package com.hangbokwatch.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerDetailDto {
    private Long id;
    private Long season;
    private Integer order;
    private String heroName;
    private String heroNameKR;
    private String killPerDeath;
    private String winRate;
    private String playTime;
    private String deathAvg;
    private String healPerLife;
    private String blockDamagePerLife;
    private String damageToHeroPerLife;
    private String damageToShieldPerLife;
    private String index1;
    private String index2;
    private String index3;
    private String index4;
    private String index5;

}
