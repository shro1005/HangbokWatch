package com.hangbokwatch.backend.dao;

import com.hangbokwatch.backend.domain.job.JobExecution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobExecutionRepository extends JpaRepository<JobExecution, Long> {
    JobExecution findJobExecutionByJobInstanceId(Long jobInstanceId);

}
