package com.hangbokwatch.backend.job;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * Quartz Schedule 에 등록된 Job 을 Spring Batch Job 으로 실행시키기 위한 Executor class.
 */
@Slf4j
@DisallowConcurrentExecution
public class BatchJobLauncher implements org.quartz.Job {
    @Autowired
    private JobLocator jobLocator;
    @Autowired
    private JobLauncher jobLauncher;

    /**
     * Quartz Job 으로 들어온 Parameter 를 Spring Batch Parameter 로 변환하고 Spring batch job을 실행
     * @param context
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            String jobName = BatchSettingHelper.getJobName(context.getMergedJobDataMap());
            log.info(" ======================= Spring Batch Start ====================== ");
            log.info("Spring Batch >>>>>>> {} 배치를 시작함 ", jobName);
            JobParameters jobParameters = BatchSettingHelper.getJobParameters(context);
            jobLauncher.run(jobLocator.getJob(jobName), jobParameters);
            log.info(" ======================= Spring Batch End ====================== ");
        }catch (SchedulerException | NoSuchJobException | JobInstanceAlreadyCompleteException | JobRestartException | JobParametersInvalidException | JobExecutionAlreadyRunningException e) {
            log.error("Spring Batch ERROR!!!! >>>>>>>> {} 에러 발생 ", e.getCause());
            log.error("====================================================\n" + e + "\n====================================================");
            throw new JobExecutionException();
        }
    }
}
