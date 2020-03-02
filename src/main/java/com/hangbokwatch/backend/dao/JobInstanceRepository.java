package com.hangbokwatch.backend.dao;

import com.hangbokwatch.backend.domain.job.JobInstance;
import com.hangbokwatch.backend.dto.JobDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JobInstanceRepository extends JpaRepository<JobInstance, Long > {
    @Query(nativeQuery = true, name = "JobInstance.GetIdAndName")
    List<JobDto> selectLastJobInstanceIdGroupByJobName();

    @Query(nativeQuery = true, name = "JobInstance.fromJobName")
    JobDto selectLastJobInstanceIdWhereJobName(String jobName);

    @Query(nativeQuery = true, value = "SELECT b.* FROM batch_job_instance b WHERE b.job_name = ?1 ORDER BY b.job_instance_id DESC offset ?2 limit ?3")
    List<JobInstance> selectAllFromJobInstanceWhereJobName(String jobName, int offset, int limit);

}
