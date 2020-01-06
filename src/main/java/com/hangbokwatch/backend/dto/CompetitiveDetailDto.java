package com.hangbokwatch.backend.dto;


import com.hangbokwatch.backend.domain.hero.*;
import com.hangbokwatch.backend.domain.player.Player;
import com.hangbokwatch.backend.domain.player.PlayerDetail;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CompetitiveDetailDto {
    private Player player;
    private List<PlayerDetail> playerDetailList;
    private Dva dva;
    private Orisa orisa;
    private Reinhardt reinhardt;
    private RoadHog roadHog;
    private Sigma sigma;
    private Winston winston;
    private WreckingBall wreckingBall;
    private Zarya zarya;
    private Ana ana;
    private Baptiste baptiste;
    private Brigitte brigitte;
    private Lucio lucio;
    private Mercy mercy;
    private Moira moira;
    private Zenyatta zenyatta;
    private int count;
}
