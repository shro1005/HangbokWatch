package com.hangbokwatch.backend.domain.job;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Entity(name = "batch_job_execution")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JobExecution {
    @Id
    @Column(name = "job_execution_id", nullable = false)
    private Long jobExecutionId;

    @Column(name = "version")
    private Long version;

    @Column(name = "job_instance_id", nullable = false)
    private Long jobInstanceId;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "status")
    private String status;

    @Column(name = "exit_code")
    private String exitCode;

    @Column(name = "exit_message")
    private String exitMessage;

    @Column(name = "last_updated")
    private LocalDateTime timeStamp;

    @Column(name = "job_configuration_location")
    private String jobConfigurationLocation;
}
