package com.hangbokwatch.backend.dto;


import com.hangbokwatch.backend.domain.hero.*;
import com.hangbokwatch.backend.domain.player.Player;
import com.hangbokwatch.backend.domain.player.PlayerDetail;
import com.hangbokwatch.backend.domain.player.Trendline;
import com.hangbokwatch.backend.domain.user.Favorite;
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
    private Genji genji;
    private Doomfist doomfist;
    private Reaper reaper;
    private Mccree mccree;
    private Mei mei;
    private Junkrat junkrat;
    private Bastion bastion;
    private Soldier76 soldier76;
    private Sombra sombra;
    private Symmetra symmetra;
    private Torbjorn torbjorn;
    private Tracer tracer;
    private Widowmaker widowmaker;
    private Pharah pharah;
    private Hanzo hanzo;
    private Ashe ashe;
    private Trendline trendline;
    private int count;
    private String favorite;
}
