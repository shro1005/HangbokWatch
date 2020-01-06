package com.hangbokwatch.backend.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity(name="season")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Season {
    @Id
    @Column(name="season", nullable = false)
    private Long season;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @Builder
    public Season(Long season, String startDate, String endDate) {
        this.season = season; this.startDate = startDate; this.endDate = endDate;
    }
}
