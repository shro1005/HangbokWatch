package com.hangbokwatch.backend.job.batch.allplayerrefrsh;

import com.hangbokwatch.backend.dao.SeasonRepository;
import com.hangbokwatch.backend.domain.player.Player;
import com.hangbokwatch.backend.dto.CompetitiveDetailDto;
import com.hangbokwatch.backend.job.BatchSettingHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.ReferenceJobFactory;
import org.springframework.batch.item.database.*;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import javax.persistence.EntityManagerFactory;
import java.text.SimpleDateFormat;


@Slf4j
@RequiredArgsConstructor  // 생성자 DI를 위한 Lombok 어노테이션
@Configuration
public class AllPlayerRefreshBatchJobConfiguration {
    public static final String JOB_NAME = "allPlayerRefreshBatch";
    public static final String BEAN_PREFIX = JOB_NAME + "_";

    private final JobBuilderFactory jobBuilderFactory ;  // 생성자 DI 받음
    private final StepBuilderFactory stepBuilderFactory ; // 생성자 DI 받음
    private final EntityManagerFactory entityManagerFactory ; // JpaItemReader애서 지정해줘야 함.

    private static final int chunkSize = 1000;

    @Autowired
    SeasonRepository seasonRepository;
    @Autowired
    JobRegistry jobRegistry;

//    @Autowired
//    public AllPlayerRefreshBatchJobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, EntityManagerFactory entityManagerFactory) {
//        this.jobBuilderFactory = jobBuilderFactory;
//        this.stepBuilderFactory = stepBuilderFactory;
//        this.entityManagerFactory = entityManagerFactory;
//    }

    @Bean
    public JobDetailFactoryBean allPlayerRefreshSchedule() throws DuplicateJobException {
        return BatchSettingHelper.jobDetailFactoryBeanBuilder()
                .job(allPlayerRefreshJob())
                .parameter("season", seasonRepository.selectSeason(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis())))
                .build();
    }

    @Bean
    public CronTriggerFactoryBean allPlayerRefreshTrigger() throws DuplicateJobException {
        return BatchSettingHelper.cronTriggerFactoryBeanBuilder()
                .cronExpression("0 15 * * * ?")
                .jobDetailFactoryBean(allPlayerRefreshSchedule())
                .name(JOB_NAME + "_trigger")
                .build();
    }

    @Bean
    public Job allPlayerRefreshJob() throws DuplicateJobException {
        final Job job = jobBuilderFactory.get(JOB_NAME)
                .start(savePlayerDetailStep())
                .build();
        ReferenceJobFactory referenceJobFactory = new ReferenceJobFactory(job);
        jobRegistry.register(referenceJobFactory);
        return job;
    }

    private final AllPlayerRefreshBatchMainProcessor mainItemProcessor;

    @Bean
    @JobScope
    public Step savePlayerDetailStep() {
        return stepBuilderFactory.get(BEAN_PREFIX + "savePlayerDetailStep")
                /** .chunk() :
                 * 첫번째 Player Reader에서 반환할 타입이며, 두번째 Player Writer에 파라미터로 넘어올 타입
                 * */
                .<Player, CompetitiveDetailDto>chunk(chunkSize)
                .reader(playerItemReader())
                .processor(mainItemProcessor)
                .writer(playerDetailsWriter())
                .build();
    }

    /** 1. JpaPagingItemReader : JPA에는 Cursor 기반 Database 접근을 지원하지 않습니다. (오직 Paging)
     *                         EntityManagerFactory를 지정하는 것 외에 JdbcPagingItemReader와 크게 다른 점은 없습니다.
     *      중요!! :  Reader 사용시 fetchSize와 ChunkSize는 같은 값을 유지해야 합
     * */
    @Bean
    public JpaPagingItemReader<Player> playerItemReader() {
        return new JpaPagingItemReaderBuilder<Player>()
                .name("playerItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("SELECT p FROM player p WHERE p.isPublic ='Y'")
                .build();
    }

    private CrawlingDataJpaItemWriter<CompetitiveDetailDto> playerDetailsWriter() {
        CrawlingDataJpaItemWriter<CompetitiveDetailDto> writer = new CrawlingDataJpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

//    private JpaItemWriter<List<PlayerDetail>> writer() {
//        JpaItemWriter<List<PlayerDetail>> writer = new JpaItemWriter<>();
//        writer.setEntityManagerFactory(entityManagerFactory);
//        return writer;
//    }
//
//    private JpaItemListWriter<PlayerDetail> playerDetailListItemWriter() {
//        JpaItemWriter<PlayerDetail> writer = new JpaItemWriter<>();
//        writer.setEntityManagerFactory(entityManagerFactory);
//
//        return new JpaItemListWriter<>(writer);
//    }



}



