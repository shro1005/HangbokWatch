package com.hangbokwatch.backend.dao;

import com.hangbokwatch.backend.domain.job.JobExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JobExecutionRepository extends JpaRepository<JobExecution, Long> {
    @Query(nativeQuery = true, value = "SELECT b.* FROM batch_job_execution b WHERE b.job_instance_id = ?1 ORDER BY b.job_execution_id DESC offset ?2 limit ?3")
    JobExecution selectJobExecutionByJobInstanceId(Long jobInstanceId, int offset, int limit);



}
