package com.hangbokwatch.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JobDto {
    private Long jobInstanceId;
    private String jobName;
    private String status;
    private String lastEndTime;
    private String lastStartTime;
    private String isNormal;

    public JobDto(Long jobInstanceId, String jobName, String status, String lastEndTime, String lastStartTime) {
        this.jobInstanceId = jobInstanceId; this.jobName = jobName; this.status = status;
        this.lastEndTime = lastEndTime; this.lastStartTime = lastStartTime;
    }

    public JobDto(Long jobInstanceId, String jobName) {
        this.jobInstanceId = jobInstanceId; this.jobName = jobName;
    }

    @Override
    public String toString() {
        return "JobDto{" +
                "jobInstanceId=" + jobInstanceId +
                ", jobName=" + jobName +
                ", status=" + status +
                ", lastStartTime='" + lastStartTime + '\'' +
                ", lastEndTime='" + lastEndTime + '\'' +
                '}';
    }
}
