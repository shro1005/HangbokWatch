package com.hangbokwatch.backend.domain.job;

import com.hangbokwatch.backend.dto.JobDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Getter
@Entity(name = "batch_job_instance")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "jobDtoMapping",
                classes = {
                        @ConstructorResult(
                                targetClass = JobDto.class,
                                columns = {
                                        @ColumnResult(name = "job_instance_id", type = Long.class),
                                        @ColumnResult(name = "job_name", type = String.class)
                                }
                        )
                }
        )
})
@NamedNativeQueries({
        @NamedNativeQuery(name = "JobInstance.GetIdAndName", query = "" +
                "select max(b.job_instance_id) as job_instance_id, b.job_name from batch_job_instance b group by b.job_name"
                , resultSetMapping = "jobDtoMapping")
        ,@NamedNativeQuery(name = "JobInstance.fromJobName", query = "" +
                "select max(b.job_instance_id) as job_instance_id, b.job_name from batch_job_instance b where b.job_name = ?1 group by b.job_name"
                , resultSetMapping = "jobDtoMapping")
})
public class JobInstance {
    @Id
    @Column(name = "job_instance_id", nullable = false)
    private Long jobInstanceId;

    @Column(name = "version")
    private Long version;

    @Column(name = "job_name")
    private String jobName;

    @Column(name = "job_key")
    private String jobKey;

}
