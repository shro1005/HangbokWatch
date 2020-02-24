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

}
