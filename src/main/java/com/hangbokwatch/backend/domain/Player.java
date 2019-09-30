package com.hangbokwatch.backend.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Entity(name="player")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Player {

    @Id
    @Column(name="battle_tag", nullable = false)
    private String battleTag;

    @Column(name="player_name", nullable = false)
    private String playerName;

    @Column(name="player_level", nullable = false)
    private Integer playerLevel;

    @Column(name="is_public", nullable = false)
    private String isPublic;

    @Column(name="platform", nullable = false)
    private String platform;

    @Column(name="portrait", nullable = false)
    private String portrait;

    @Column(name="tank_rating_point")
    private Integer tankRatingPoint;

    @Column(name="deal_rating_point")
    private Integer dealRatingPoint;

    @Column(name="heal_rating_point")
    private Integer healRatingPoint;

    @Column(name="win_game")
    private Integer winGame;

    @Column(name="lose_game")
    private Integer loseGame;

    @Column(name="draw_game")
    private Integer drawGame;

    @Column(name="most_hero1")
    private String mostHero1;

    @Column(name="most_hero2")
    private String mostHero2;

    @Column(name="most_hero3")
    private String mostHero3;

    @CreationTimestamp
    @Column(name="rgt_dtm", nullable = true)
    private LocalDateTime rgtDtm;

    @UpdateTimestamp
    @Column(name="udt_dtm", nullable = true)
    private LocalDateTime udtDtm;

    @Builder
    public Player(String battleTag, String playerName, Integer playerLevel, String isPublic, String platform,
                  String portrait, Integer tankRatingPoint, Integer dealRatingPoint, Integer healRatingPoint,
                  Integer winGame, Integer loseGame, Integer drawGame, String mostHero1, String mostHero2, String mostHero3) {
        this.battleTag = battleTag; this.playerName = playerName; this.playerLevel = playerLevel; this.isPublic = isPublic;
        this.platform = platform; this.portrait = portrait; this.tankRatingPoint = tankRatingPoint; this.dealRatingPoint = dealRatingPoint;
        this.healRatingPoint = healRatingPoint; this.winGame = winGame; this.loseGame = loseGame; this.drawGame = drawGame;
        this.mostHero1 = mostHero1; this.mostHero2 = mostHero2; this.mostHero3 = mostHero3;
    }

    @Builder
    public Player(String battleTag, String playerName, Integer playerLevel, String isPublic, String platform, String portrait) {
        this.battleTag = battleTag; this.playerName = playerName; this.playerLevel = playerLevel; this.isPublic = isPublic;
        this.platform = platform; this.portrait = portrait;
    }

}
