package com.hangbokwatch.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
public class PlayerListDto implements Comparable<PlayerListDto> {
    private Long id;
    private String battleTag;
    private String playerName;
    private String forUrl;
    private Integer playerLevel;
    private String isPublic;
    private String platform;
    private String portrait;
    private Integer tankRatingPoint;
    private Integer dealRatingPoint;
    private Integer healRatingPoint;
    private String tankRatingImg;
    private String dealRatingImg;
    private String healRatingImg;
    private Integer winGame;
    private Integer loseGame;
    private Integer drawGame;
    private String winRate;
    private String mostHero1;
    private String mostHero2;
    private String mostHero3;
    private Integer cnt;
    private String udtDtm;
    private List<PlayerListDto> list;

    @Builder
    public PlayerListDto(Long id, String battleTag, String playerName, String forUrl, Integer playerLevel, String isPublic, String platform,
                         String portrait, Integer tankRatingPoint, Integer dealRatingPoint, Integer healRatingPoint,
                         String tankRatingImg, String dealRatingImg, String healRatingImg, Integer winGame, Integer loseGame,
                         Integer drawGame, String winRate, String mostHero1, String mostHero2, String mostHero3, String udtDtm){ //) {
        this.id = id;
        this.playerName = playerName;
        this.playerLevel = playerLevel;
        this.isPublic = isPublic;
        this.platform = platform;
        this.portrait = portrait;
        this.tankRatingPoint = tankRatingPoint;
        this.dealRatingPoint = dealRatingPoint;
        this.healRatingPoint = healRatingPoint;
        this.tankRatingImg = tankRatingImg;
        this.dealRatingImg = dealRatingImg;
        this.healRatingImg = healRatingImg;
        this.winGame = winGame;
        this.loseGame = loseGame;
        this.drawGame = drawGame;
        this.winRate = winRate;
        this.mostHero1 = mostHero1;
        this.mostHero2 = mostHero2;
        this.mostHero3 = mostHero3;
        this.battleTag = battleTag;
        this.forUrl = forUrl;
        this.udtDtm = udtDtm;
//        this.list = list;
    }

    @Builder
    public PlayerListDto(Long id, String battleTag, String playerName, String forUrl, Integer playerLevel, String isPublic, String platform,
                         String portrait, Integer tankRatingPoint, Integer dealRatingPoint, Integer healRatingPoint, String udtDtm) {
        this.id = id;
        this.playerName = playerName;
        this.playerLevel = playerLevel;
        this.isPublic = isPublic;
        this.platform = platform;
        this.portrait = portrait;
        this.battleTag = battleTag;
        this.tankRatingPoint = tankRatingPoint;
        this.forUrl = forUrl;
        this.dealRatingPoint = dealRatingPoint;
        this.healRatingPoint = healRatingPoint;
        this.udtDtm = udtDtm;
    }

    @Builder
    public PlayerListDto(){}

    @Override
    public int compareTo(PlayerListDto o) {
        if (this.isPublic.compareTo(o.isPublic) > 0) {
            return -1;
        } else if (this.isPublic.compareTo(o.isPublic) < 0) {
            return 1;
        } else {
            if ((o.getTankRatingPoint() + o.getDealRatingPoint() + o.getHealRatingPoint()) / o.getCnt() - (this.getTankRatingPoint() + this.getDealRatingPoint() + this.getHealRatingPoint()) / this.getCnt() != 0) {
                return (o.getTankRatingPoint() + o.getDealRatingPoint() + o.getHealRatingPoint()) / o.getCnt() - (this.getTankRatingPoint() + this.getDealRatingPoint() + this.getHealRatingPoint()) / this.getCnt();
            } else {
                return o.getPlayerLevel() - this.getPlayerLevel();
            }
        }
    }

    @Override
    public String toString() {
        return "PlayerListDto{" +
                "battleTag='" + battleTag + '\'' +
                ", playerName='" + playerName + '\'' +
                ", forUrl='" + forUrl + '\'' +
                ", playerLevel=" + playerLevel +
                ", isPublic='" + isPublic + '\'' +
                ", platform='" + platform + '\'' +
                ", portrait='" + portrait + '\'' +
                ", tankRatingPoint=" + tankRatingPoint +
                ", dealRatingPoint=" + dealRatingPoint +
                ", healRatingPoint=" + healRatingPoint +
                '}';
    }
}
