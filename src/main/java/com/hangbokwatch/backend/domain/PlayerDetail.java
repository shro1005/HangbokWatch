package com.hangbokwatch.backend.domain;


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.LocalDateTime;

public class PlayerDetail {
    @Id
    @Column(name="battle_tag", nullable = false)
    private String battleTag;

    @Column(name="player_name", nullable = false)
    private String playerName;

    @Column(name="player_level", nullable = false)
    private Integer playerLevel;

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
}
