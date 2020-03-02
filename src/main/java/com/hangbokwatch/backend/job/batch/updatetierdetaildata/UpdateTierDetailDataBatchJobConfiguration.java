package com.hangbokwatch.backend.job.batch.updatetierdetaildata;

import com.hangbokwatch.backend.dao.SeasonRepository;
import com.hangbokwatch.backend.domain.player.Player;
import com.hangbokwatch.backend.dto.CompetitiveDetailDto;
import com.hangbokwatch.backend.job.BatchSettingHelper;
import com.hangbokwatch.backend.job.batch.CrawlingDataJpaItemWriter;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import javax.persistence.EntityManagerFactory;
import java.text.SimpleDateFormat;
import java.util.List;

@Slf4j
@RequiredArgsConstructor  // 생성자 DI를 위한 Lombok 어노테이션
@Configuration
public class UpdateTierDetailDataBatchJobConfiguration {
    public static final String JOB_NAME = "updateTierDetailDataBatch";
    public static final String BEAN_PREFIX = JOB_NAME + "_";

    private final JobBuilderFactory jobBuilderFactory ;  // 생성자 DI 받음
    private final StepBuilderFactory stepBuilderFactory ; // 생성자 DI 받음
    private final EntityManagerFactory entityManagerFactory ; // JpaItemReader애서 지정해줘야 함.

    private static final int chunkSize = 300;

    @Autowired
    SeasonRepository seasonRepository;
    @Autowired
    JobRegistry jobRegistry;

    @Bean
    public JobDetailFactoryBean updateTierDetailSchedule() throws DuplicateJobException {
        return BatchSettingHelper.jobDetailFactoryBeanBuilder()
                .job(updateTierDetailDataJob())
                .parameter("season", seasonRepository.selectSeason(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis())))
                .build();
    }

    @Bean
    public CronTriggerFactoryBean updateTierDetailTrigger() throws DuplicateJobException {
        return BatchSettingHelper.cronTriggerFactoryBeanBuilder()
                .cronExpression("0 50 4 * * ?")
                .jobDetailFactoryBean(updateTierDetailSchedule())
                .name(JOB_NAME + "_trigger")
                .build();
    }

    @Bean
    public Job updateTierDetailDataJob() throws DuplicateJobException {
        final Job job = jobBuilderFactory.get(JOB_NAME)
                .start(updateGrandMasterTierDetailDataStep(null))
                .next(updateMasterTierDetailDataStep(null))
                .next(updateDiaTierDetailDataStep(null))
                .next(updatePlatinumTierDetailDataStep(null))
                .next(updateGoldTierDetailDataStep(null))
                .next(updateSilverTierDetailDataStep(null))
                .next(updateBronzeTierDetailDataStep(null))
                .build();
        ReferenceJobFactory referenceJobFactory = new ReferenceJobFactory(job);
        jobRegistry.register(referenceJobFactory);
        return job;
    }

    @Bean
    @JobScope
    public Step updateGrandMasterTierDetailDataStep(@Value("#{jobParameters[season]}") Long season) {
        return stepBuilderFactory.get(BEAN_PREFIX + "updateGrandMasterTierDetailDataStep")
                .<List<Player>, CompetitiveDetailDto>chunk(chunkSize)
                .reader(grandMasterItemReader())
                .processor(updateTierDetailProcessor.setMinMax(4000, 6000).setSeason(season))
                .writer(playerDetailsWriter())
                .build();
    }

    @Bean
    @JobScope
    public Step updateMasterTierDetailDataStep(@Value("#{jobParameters[season]}") Long season) {
        return stepBuilderFactory.get(BEAN_PREFIX + "updateMasterTierDetailDataStep")
                .<List<Player>, CompetitiveDetailDto>chunk(chunkSize)
                .reader(masterItemReader())
                .processor(updateTierDetailProcessor.setMinMax(3500, 3999).setSeason(season))
                .writer(playerDetailsWriter())
                .build();
    }

    @Bean
    @JobScope
    public Step updateDiaTierDetailDataStep(@Value("#{jobParameters[season]}") Long season) {
        return stepBuilderFactory.get(BEAN_PREFIX + "updateDiaTierDetailDataStep")
                .<List<Player>, CompetitiveDetailDto>chunk(chunkSize)
                .reader(diaItemReader())
                .processor(updateTierDetailProcessor.setMinMax(3000, 3499).setSeason(season))
                .writer(playerDetailsWriter())
                .build();
    }

    @Bean
    @JobScope
    public Step updatePlatinumTierDetailDataStep(@Value("#{jobParameters[season]}") Long season) {
        return stepBuilderFactory.get(BEAN_PREFIX + "updatePlatinumTierDetailDataStep")
                .<List<Player>, CompetitiveDetailDto>chunk(chunkSize)
                .reader(platinumItemReader())
                .processor(updateTierDetailProcessor.setMinMax(2500, 2999).setSeason(season))
                .writer(playerDetailsWriter())
                .build();
    }

    @Bean
    @JobScope
    public Step updateGoldTierDetailDataStep(@Value("#{jobParameters[season]}") Long season) {
        return stepBuilderFactory.get(BEAN_PREFIX + "updateGoldTierDetailDataStep")
                .<List<Player>, CompetitiveDetailDto>chunk(chunkSize)
                .reader(goldItemReader())
                .processor(updateTierDetailProcessor.setMinMax(2000, 2499).setSeason(season))
                .writer(playerDetailsWriter())
                .build();
    }

    @Bean
    @JobScope
    public Step updateSilverTierDetailDataStep(@Value("#{jobParameters[season]}") Long season) {
        return stepBuilderFactory.get(BEAN_PREFIX + "updateSilverTierDetailDataStep")
                .<List<Player>, CompetitiveDetailDto>chunk(chunkSize)
                .reader(silverItemReader())
                .processor(updateTierDetailProcessor.setMinMax(1500, 1999).setSeason(season))
                .writer(playerDetailsWriter())
                .build();
    }

    @Bean
    @JobScope
    public Step updateBronzeTierDetailDataStep(@Value("#{jobParameters[season]}") Long season) {
        return stepBuilderFactory.get(BEAN_PREFIX + "updateBronzeTierDetailDataStep")
                .<List<Player>, CompetitiveDetailDto>chunk(chunkSize)
                .reader(bronzeItemReader())
                .processor(updateTierDetailProcessor.setMinMax(1, 1499).setSeason(season))
                .writer(playerDetailsWriter())
                .build();
    }

    private final UpdateTierDetailDataBatchJobProcessor updateTierDetailProcessor;

    @Bean
    public JpaPagingResultListItemReader<List<Player>> grandMasterItemReader() {
        return new JpaPagingResultListItemReaderBuilder<List<Player>>()
                .name("grandMasterItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("SELECT p FROM player p WHERE p.isPublic ='Y' AND (p.tankRatingPoint >= 4000 OR p.dealRatingPoint >= 4000 OR p.healRatingPoint >= 4000)")
                .maxItemCount(1)
                .build();
    }

    @Bean
    public JpaPagingResultListItemReader<List<Player>> masterItemReader() {
        return new JpaPagingResultListItemReaderBuilder<List<Player>>()
                .name("masterItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("SELECT p FROM player p WHERE p.isPublic ='Y' AND (p.tankRatingPoint BETWEEN 3500 AND 3999 OR p.dealRatingPoint BETWEEN 3500 AND 3999 OR p.healRatingPoint BETWEEN 3500 AND 3999)")
                .maxItemCount(1)
                .build();
    }

    @Bean
    public JpaPagingResultListItemReader<List<Player>> diaItemReader() {
        return new JpaPagingResultListItemReaderBuilder<List<Player>>()
                .name("diaItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("SELECT p FROM player p WHERE p.isPublic ='Y' AND (p.tankRatingPoint BETWEEN 3000 AND 3499 OR p.dealRatingPoint BETWEEN 3000 AND 3499 OR p.healRatingPoint BETWEEN 3000 AND 3499)")
                .maxItemCount(1)
                .build();
    }

    @Bean
    public JpaPagingResultListItemReader<List<Player>> platinumItemReader() {
        return new JpaPagingResultListItemReaderBuilder<List<Player>>()
                .name("platinumItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("SELECT p FROM player p WHERE p.isPublic ='Y' AND (p.tankRatingPoint BETWEEN 2500 AND 2999 OR p.dealRatingPoint BETWEEN 2500 AND 2999 OR p.healRatingPoint BETWEEN 2500 AND 2999)")
                .maxItemCount(1)
                .build();
    }

    @Bean
    public JpaPagingResultListItemReader<List<Player>> goldItemReader() {
        return new JpaPagingResultListItemReaderBuilder<List<Player>>()
                .name("goldItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("SELECT p FROM player p WHERE p.isPublic ='Y' AND (p.tankRatingPoint BETWEEN 2000 AND 2499 OR p.dealRatingPoint BETWEEN 2000 AND 2499 OR p.healRatingPoint BETWEEN 2000 AND 2499)")
                .maxItemCount(1)
                .build();
    }

    @Bean
    public JpaPagingResultListItemReader<List<Player>> silverItemReader() {
        return new JpaPagingResultListItemReaderBuilder<List<Player>>()
                .name("silverItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("SELECT p FROM player p WHERE p.isPublic ='Y' AND (p.tankRatingPoint BETWEEN 1500 AND 1999 OR p.dealRatingPoint BETWEEN 1500 AND 1999 OR p.healRatingPoint BETWEEN 1500 AND 1999)")
                .maxItemCount(1)
                .build();
    }

    @Bean
    public JpaPagingResultListItemReader<List<Player>> bronzeItemReader() {
        return new JpaPagingResultListItemReaderBuilder<List<Player>>()
                .name("bronzeItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("SELECT p FROM player p WHERE p.isPublic ='Y' AND (p.tankRatingPoint BETWEEN 1 AND 1499 OR p.dealRatingPoint BETWEEN 1 AND 1499 OR p.healRatingPoint BETWEEN 1 AND 1499)")
                .maxItemCount(1)
                .build();
    }

    private CrawlingDataJpaItemWriter<CompetitiveDetailDto> playerDetailsWriter() {
        CrawlingDataJpaItemWriter<CompetitiveDetailDto> writer = new CrawlingDataJpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }


}
