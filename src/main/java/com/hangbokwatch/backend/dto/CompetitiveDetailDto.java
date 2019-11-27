package com.hangbokwatch.backend.dto;


import com.hangbokwatch.backend.domain.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompetitiveDetailDto {
    private Player player;
    private Dva dva;
    private Orisa orisa;
    private Reinhardt reinhardt;
    private RoadHog roadHog;
    private Sigma sigma;
    private Winston winston;
    private WreckingBall wreckingBall;
    private Zarya zarya;
}
