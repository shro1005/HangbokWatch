package com.hangbokwatch.backend.dto;

import com.hangbokwatch.backend.domain.Season;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeasonDto {
    private Long season;
    private String startDate;
    private String endDate;
    private String isSuccess;

    public SeasonDto(Long season, String startDate, String endDate) {
        this.season = season; this.startDate = startDate; this.endDate = endDate;
    }
}
