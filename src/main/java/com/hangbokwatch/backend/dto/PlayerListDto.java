package com.hangbokwatch.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;

@Getter
@Setter
public class PlayerListDto implements Comparable<PlayerListDto> {
    private String battleTag;
    private String playerName;
    private Integer playerLevel;
    private String isPublic;
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
    private String mostHero2;
    private String mostHero3;

    @Builder
    public PlayerListDto(String battleTag, String playerName, Integer playerLevel, String isPublic, String platform, String portrait,
                         Integer tankRatingPoint, Integer dealRatingPoint, Integer healRatingPoint, Integer winGame,
                         Integer loseGame, Integer drawGame, String mostHero1, String mostHero2, String mostHero3) {
        this.playerName = playerName; this.playerLevel = playerLevel; this.isPublic = isPublic; this.platform = platform;
        this.portrait = portrait; this.tankRatingPoint = tankRatingPoint; this.dealRatingPoint = dealRatingPoint;
        this.healRatingPoint = healRatingPoint; this.winGame = winGame; this.loseGame = loseGame; this.drawGame = drawGame;
        this.mostHero1 = mostHero1; this.mostHero2 = mostHero2; this.mostHero3 = mostHero3; this.battleTag = battleTag;
    }

    @Builder
    public PlayerListDto(String battleTag, String playerName, Integer playerLevel, String isPublic, String platform, String portrait) {
        this.playerName = playerName; this.playerLevel = playerLevel; this.isPublic = isPublic;
        this.platform = platform; this.portrait = portrait; this.battleTag = battleTag;
    }

    @Override
    public int compareTo(PlayerListDto o) {
        if(this.isPublic.compareTo(o.isPublic)>0) {
            return -1;
        }else if(this.isPublic.compareTo(o.isPublic)<0) {
            return 1;
        }else {
            if ((this.getTankRatingPoint() + this.getDealRatingPoint() + this.getHealRatingPoint())/3 - (o.getTankRatingPoint() + o.getDealRatingPoint() + o.getHealRatingPoint())/3 !=0) {
                return (this.getTankRatingPoint() + this.getDealRatingPoint() + this.getHealRatingPoint())/3 - (o.getTankRatingPoint() + o.getDealRatingPoint() + o.getHealRatingPoint())/3;
            }else {
                return this.getPlayerLevel() - o.getPlayerLevel();
            }
        }
    }

    @Override
    public String toString() {
        return "PlayerListDto{" +
                "battleTag='" + battleTag + '\'' +
                ", playerName='" + playerName + '\'' +
                ", playerLevel=" + playerLevel +
                ", isPublic='" + isPublic + '\'' +
                ", platform='" + platform + '\'' +
                ", portrait='" + portrait + '\'' +
                '}';
    }
}
