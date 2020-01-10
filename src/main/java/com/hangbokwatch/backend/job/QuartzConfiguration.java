package com.hangbokwatch.backend.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import javax.sql.DataSource;
import java.util.List;


@Slf4j
//@EnableConfigurationProperties(QuartzProperties.class)
@EnableBatchProcessing
public class QuartzConfiguration {
    /**
     * JobRegistry에 job를 자동등록하기 위한 설정
     *   @param jobRegistry ths Spring Batch Job Registry
     *   @return JobRegistryBeanPostProcessor
     */
    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        return jobRegistryBeanPostProcessor;
    }

    /**
     * Quartz Schedule Job에 의존성 주입
     *  - QuartzJobBean를 사용하는데신, 필요한 Bean의 Injection을 Spring에게 위임
     *   @param beanFactory application context beanFactory
     *   @return Jobfactory
     */
//    @Bean
//    public JobFactory jobFactory(AutowireCapableBeanFactory beanFactory) {
//        return new SpringBeanJobFactory(){
//            @Override
//            protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
//                Object job = super.createJobInstance(bundle);
//                beanFactory.autowireBean(job);
//                return job;
//            }
//        };
//    }

    /**
     * Scheduler 전체를 관리하는 Manager.
     *
     * @param datasource Spring datasource
     * @return the scheduler factory bean
     * @throws Exception the exception
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(DataSource datasource, JobFactory jobFactory, Trigger[] registryTrigger) throws Exception {

        SchedulerFactoryBean factory = new SchedulerFactoryBean();

        factory.setSchedulerName("SampleProject-0.0.1");
        //Register JobFactory
        factory.setJobFactory(jobFactory);
        //Graceful Shutdown 을 위한 설정으로 Job 이 완료될 때까지 Shutdown 을 대기하는 설정
        factory.setWaitForJobsToCompleteOnShutdown(true);
        //Job Detail 데이터 Overwrite 유무
        factory.setOverwriteExistingJobs(true);
        //Register QuartzProperties
//        factory.setQuartzProperties(quartzProperties.toProperties());
        //Schedule 관리를 Spring Datasource 에 위임
        factory.setDataSource(datasource);
        //Register Triggers
        factory.setTriggers(registryTrigger);

        return factory;
    }

    /**
     * Scheduler 에 Trigger 를 자동으로 등록하기 위한 설정.
     * @param cronTriggerFactoryBeanList
     * @return
     */
    @Bean
    public Trigger[] registryTrigger(List<CronTriggerFactoryBean> cronTriggerFactoryBeanList) {
        return cronTriggerFactoryBeanList.stream().map(CronTriggerFactoryBean::getObject).toArray(Trigger[]::new);
    }

    /**
     * DataSource에 대한 설정을 Spring Framework에게 위임했다면 이 설정을 쓸 수 없습니다.
     * 그 이유는 Spring Framework가 종료될 때 독립적으로 수행되고 있는 Quartz Job을 기다리지 않기 때문입니다.
     * 기다리지 않기 때문에 Spring Framework는 DataSource Connection을 close할 것이고, Quartz는 Job이 정상적으로 완료되었다고 할지라도 DataSource 에 정보를 업데이트하지 못한 채 종료가 됩니다.
     * 따라서 Spring Framework 가 종료될 때 Quartz 상태를 체크하고 기다리거나 종료하는 역할을 구현.
     * @param schedulerFactoryBean
     * @return
     */
    @Bean
    public SmartLifecycle gracefulShutdownHookForQuartz(SchedulerFactoryBean schedulerFactoryBean) {
        return new SmartLifecycle() {
            private boolean isRunning = false;

            @Override
            public boolean isAutoStartup() {
                return true;
            }

            @Override
            public void stop(Runnable callback) {
                stop();
                log.info("Spring container is shutting down.");
                callback.run();
            }

            @Override
            public void start() {
                log.info("Quartz Graceful Shutdown Hook started.");
                isRunning = true;
            }

            @Override
            public void stop() {
                isRunning = false;
                try {
                    log.info("Quartz Graceful Shutdown... ");
                    schedulerFactoryBean.destroy();
                } catch (SchedulerException e) {
                    try {
                        log.info(
                                "Error shutting down Quartz: " + e.getMessage(), e);
                        schedulerFactoryBean.getScheduler().shutdown(false);
                    } catch (SchedulerException ex) {
                        log.error("Unable to shutdown the Quartz scheduler.", ex);
                    }
                }
            }

            @Override
            public boolean isRunning() {
                return isRunning;
            }

            @Override
            public int getPhase() {
                return Integer.MAX_VALUE;
            }
        };
    }


//    @Bean
//    public JobDetailFactoryBean jobDetailFactoryBean() {
//        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
//        factoryBean.setJobClass(QuartzJobLauncher.class);
//        Map map = new HashMap();
//        map.put("jobName", "allPlayerRefreshBatch");
//        map.put("jobLauncher", jobLauncher);
//        map.put("jobLocator", jobLocator);
//        factoryBean.setJobDataAsMap(map);
//        factoryBean.setGroup("player_refresh_group");
//        factoryBean.setName("player_refresh_job");
//        return factoryBean;
//    }
//
//    @Bean
//    public CronTriggerFactoryBean cronTriggerFactoryBean() {
//        CronTriggerFactoryBean ctFactoryBean = new CronTriggerFactoryBean();
//        ctFactoryBean.setJobDetail(jobDetailFactoryBean().getObject());
//        ctFactoryBean.setStartDelay(3000);
//        ctFactoryBean.setGroup("player_refresh_cron_group");
//        ctFactoryBean.setName("all_player_refresh_cron_trigger");
//        ctFactoryBean.setCronExpression("0 30 21 * * ?");
//        return ctFactoryBean;
//    }
//
//    @Bean
//    public SchedulerFactoryBean schedulerFactoryBean() {
//        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
//        scheduler.setTriggers(cronTriggerFactoryBean().getObject());
//        return scheduler;
//    }

}
