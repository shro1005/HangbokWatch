package com.hangbokwatch.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerRankingDto {
    private Long id;
    private String battleTag;
    private String playerName;
    private String portrait;
    private Integer tankRatingPoint;
    private Integer dealRatingPoint;
    private Integer healRatingPoint;
    private Integer winGame;
    private Integer loseGame;
    private Integer drawGame;
    private String playTime;
    private String spentOnFire;
    private Integer envKill;
    private String score;
    private String className;

    public PlayerRankingDto(Long id, String battleTag, String playerName, String portrait, Integer tankRatingPoint,
                         Integer dealRatingPoint, Integer healRatingPoint, Integer winGame, Integer loseGame,
                         Integer drawGame, String playTime, String spentOnFire, Integer envKill) {
        this.id = id; this.battleTag = battleTag; this.playerName = playerName; this.portrait = portrait; this.tankRatingPoint = tankRatingPoint;
        this.dealRatingPoint = dealRatingPoint; this.healRatingPoint = healRatingPoint; this.winGame = winGame;
        this.loseGame = loseGame; this.drawGame = drawGame; this.playTime = playTime; this.spentOnFire = spentOnFire; this.envKill = envKill;
    }

    public PlayerRankingDto(Long id, String battleTag, String playerName, String portrait, String score, String className) {
        this.id = id; this.battleTag = battleTag; this.playerName = playerName; this.portrait = portrait; this.score = score; this.className = className;
    }
}
