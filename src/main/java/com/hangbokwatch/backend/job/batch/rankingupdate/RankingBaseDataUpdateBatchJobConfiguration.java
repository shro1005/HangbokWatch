package com.hangbokwatch.backend.job.batch.rankingupdate;

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
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import javax.persistence.EntityManagerFactory;

@Slf4j
@RequiredArgsConstructor  // 생성자 DI를 위한 Lombok 어노테이션
@Configuration
public class RankingBaseDataUpdateBatchJobConfiguration {
    public static final String JOB_NAME = "rankingBaseDataUpdateBatch";
    public static final String BEAN_PREFIX = JOB_NAME + "_";

    private final JobBuilderFactory jobBuilderFactory ;  // 생성자 DI 받음
    private final StepBuilderFactory stepBuilderFactory ; // 생성자 DI 받음
    private final EntityManagerFactory entityManagerFactory ; // JpaItemReader애서 지정해줘야 함.

    private static final int chunkSize = 1000;

    @Autowired
    JobRegistry jobRegistry;

    @Bean
    public CronTriggerFactoryBean rankingBaseUpdateTrigger() throws DuplicateJobException {
        return BatchSettingHelper.cronTriggerFactoryBeanBuilder()
                .name(JOB_NAME + "_trigger")
                .cronExpression("0 30 3 ? * MON")
                .jobDetailFactoryBean(rankingBaseUpdateJobDetail())
                .build();
    }

    @Bean
    public JobDetailFactoryBean rankingBaseUpdateJobDetail() throws DuplicateJobException {
        return BatchSettingHelper.jobDetailFactoryBeanBuilder()
                .job(updateRankingBaseDataJob())
                .build();
    }

    @Bean
    public Job updateRankingBaseDataJob() throws DuplicateJobException {
        final Job job = jobBuilderFactory.get(JOB_NAME)
                .start(updateRankingBaseDataStep())
                .build();
        ReferenceJobFactory referenceJobFactory = new ReferenceJobFactory(job);
        jobRegistry.register(referenceJobFactory);
        return job;
    }

    @Bean
    @JobScope
    public Step updateRankingBaseDataStep() {
        return stepBuilderFactory.get(BEAN_PREFIX + "updateRankingBaseDataStep")
                .<Player, CompetitiveDetailDto>chunk(chunkSize)
                .reader(playerForRankingItemReader())
                .processor(mainItemProcessor)
                .writer(rankingDataWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<Player> playerForRankingItemReader() {
        return new JpaPagingItemReaderBuilder<Player>()
                .name("playerItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("SELECT p FROM player p WHERE p.isPublic ='Y'")
                .build();
    }

    private final RankingBaseDataUpdateBatchMainProcessor mainItemProcessor;

    private RankingBaseUpdateBatchWriter<CompetitiveDetailDto> rankingDataWriter() {
        RankingBaseUpdateBatchWriter<CompetitiveDetailDto> writer = new RankingBaseUpdateBatchWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
}
