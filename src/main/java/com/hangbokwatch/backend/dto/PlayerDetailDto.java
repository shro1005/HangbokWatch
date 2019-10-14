package com.hangbokwatch.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerDetailDto {
    private Long id;
    private String battleTag;
    private String playerName;
    private Integer playerLevel;
    private String platform;
    private String portrait;
    private Integer tankRatingPoint;
    private Integer dealRatingPoint;
    private Integer healRatingPoint;
    private Integer winGame;
    private Integer loseGame;
    private Integer drawGame;
    private Integer winRate;
    private String mostHero1;

}
