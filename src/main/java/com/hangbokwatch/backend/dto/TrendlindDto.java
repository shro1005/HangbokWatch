package com.hangbokwatch.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrendlindDto {
    private Long id;
    private String udtDtm;
    private Integer tankRatingPoint;
    private Integer dealRatingPoint;
    private Integer healRatingPoint;
    private Integer tankWinGame;
    private Integer tankLoseGame;
    private Integer dealWinGame;
    private Integer dealLoseGame;
    private Integer healWinGame;
    private Integer healLoseGame;
}
