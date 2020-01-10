package com.hangbokwatch.backend;

import com.hangbokwatch.backend.dao.SeasonRepository;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@EnableBatchProcessing  // 배치 기능 활성화
@SpringBootApplication
public class BackendApplication {
    @Autowired
    SeasonRepository seasonRepository;

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml";
//            + "/home/ec2-user/build/config/real-application.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(BackendApplication.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }

}
