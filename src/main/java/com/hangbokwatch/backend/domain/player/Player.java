package com.hangbokwatch.backend.domain.player;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity(name="player")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Player {

    @Id
    @Column(name="id", nullable = false)
    private Long id;

    @Column(name="battle_tag", nullable = false)
    private String battleTag;

    @Column(name="player_name", nullable = false)
    private String playerName;

    @Column(name="player_level", nullable = false)
    private Integer playerLevel;

    @Column(name="for_url", nullable = false)
    private String forUrl;

    @Column(name="is_public", nullable = false)
    private String isPublic;

    @Column(name="platform", nullable = false)
    private String platform;

    @Column(name="portrait", nullable = false)
    private String portrait;

    @Column(name="tank_rating_point")
    private Integer tankRatingPoint;

    @Column(name="tank_rating_img")
    private String tankRatingImg;

    @Column(name = "tank_win_game")
    private Integer tankWinGame;

    @Column(name = "tank_lose_game")
    private Integer tankLoseGame;

    @Column(name="deal_rating_point")
    private Integer dealRatingPoint;

    @Column(name="deal_rating_img")
    private String dealRatingImg;

    @Column(name = "deal_win_game")
    private Integer dealWinGame;

    @Column(name = "deal_lose_game")
    private Integer dealLoseGame;

    @Column(name="heal_rating_point")
    private Integer healRatingPoint;

    @Column(name="heal_rating_img")
    private String healRatingImg;

    @Column(name = "heal_win_game")
    private Integer healWinGame;

    @Column(name = "heal_lose_game")
    private Integer healLoseGame;

    @Column(name="total_avg_rating_point")
    private Integer totalAvgRatingPoint;

    @Column(name="win_game")
    private Integer winGame;

    @Column(name="lose_game")
    private Integer loseGame;

    @Column(name="draw_game")
    private Integer drawGame;

    @Column(name="play_time")
    private Long playTime;

    @Column(name = "spent_on_fire")
    private Long spentOnFire;

    @Column(name = "env_kill")
    private Integer envKill;

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

    public Player(Long id, String battleTag, String playerName, Integer playerLevel, String forUrl, String isPublic, String platform,
                  String portrait, Integer tankRatingPoint, Integer dealRatingPoint, Integer healRatingPoint, Integer totalAvgRatingPoint,
                  String tankRatingImg, String dealRatingImg, String healRatingImg,
                  Integer tankWinGame, Integer tankLoseGame, Integer dealWinGame, Integer dealLoseGame, Integer healWinGame, Integer healLoseGame,
                  Integer winGame, Integer loseGame, Integer drawGame, Long playTime, Long spentOnFire, Integer envKill,
                  String mostHero1, String mostHero2, String mostHero3) {
        this.id = id ;this.battleTag = battleTag; this.playerName = playerName; this.playerLevel = playerLevel; this.isPublic = isPublic;
        this.platform = platform; this.portrait = portrait; this.tankRatingPoint = tankRatingPoint; this.dealRatingPoint = dealRatingPoint;
        this.healRatingPoint = healRatingPoint; this.totalAvgRatingPoint = totalAvgRatingPoint; this.tankRatingImg = tankRatingImg; this.dealRatingImg = dealRatingImg; this.healRatingImg = healRatingImg;
        this.tankWinGame = tankWinGame; this.tankLoseGame = tankLoseGame; this.dealWinGame = dealWinGame; this.dealLoseGame = dealLoseGame; this.healWinGame = healWinGame; this.healLoseGame = healLoseGame;
        this.winGame = winGame; this.loseGame = loseGame; this.drawGame = drawGame; this.playTime = playTime; this.spentOnFire = spentOnFire; this.envKill = envKill;
        this.mostHero1 = mostHero1; this.mostHero2 = mostHero2; this.mostHero3 = mostHero3; this.forUrl = forUrl;
    }

    public Player(Long id, String battleTag, String playerName, Integer playerLevel, String forUrl, String isPublic, String platform, String portrait) {
        this.id = id ;this.battleTag = battleTag; this.playerName = playerName; this.playerLevel = playerLevel; this.isPublic = isPublic;
        this.platform = platform; this.portrait = portrait; this.forUrl = forUrl;
    }
}
