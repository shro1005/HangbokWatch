package com.hangbokwatch.backend.domain.player;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Getter
@Entity(name="trendline")
@IdClass(TrendlineId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Trendline {
    @Id
    @Column(name="id", nullable = false)
    private Long id;

    @Id
    @Column(name = "udt_dtm", nullable = false)
    private String udtDtm;

    @Column(name="tank_rating_point")
    private Integer tankRatingPoint;

    @Column(name="deal_rating_point")
    private Integer dealRatingPoint;

    @Column(name="heal_rating_point")
    private Integer healRatingPoint;

    @Column(name = "tank_win_game")
    private Integer tankWinGame;

    @Column(name = "tank_lose_game")
    private Integer tankLoseGame;

    @Column(name = "deal_win_game")
    private Integer dealWinGame;

    @Column(name = "deal_lose_game")
    private Integer dealLoseGame;

    @Column(name = "heal_win_game")
    private Integer healWinGame;

    @Column(name = "heal_lose_game")
    private Integer healLoseGame;

    @Builder
    public Trendline(Long id, String udtDtm, Integer tankRatingPoint, Integer dealRatingPoint, Integer healRatingPoint,
                  Integer tankWinGame, Integer tankLoseGame, Integer dealWinGame, Integer dealLoseGame, Integer healWinGame, Integer healLoseGame) {
        this.id = id ;this.udtDtm = udtDtm; this.tankRatingPoint = tankRatingPoint; this.dealRatingPoint = dealRatingPoint; this.healRatingPoint = healRatingPoint;
        this.tankWinGame = tankWinGame; this.tankLoseGame = tankLoseGame; this.dealWinGame = dealWinGame; this.dealLoseGame = dealLoseGame; this.healWinGame = healWinGame; this.healLoseGame = healLoseGame;
    }
}
