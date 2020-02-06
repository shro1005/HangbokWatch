package com.hangbokwatch.backend.domain.player;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.time.LocalDateTime;

@Getter
@Entity(name="forranking")
@IdClass(PlayerForRankingId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlayerForRanking {
    @Id
    @Column(name="id", nullable = false)
    private Long id;

    @Column(name="player_level")
    private Integer playerLevel;

    @Column(name="tank_rating_point")
    private Integer tankRatingPoint;

    @Column(name = "tank_win_game")
    private Integer tankWinGame;

    @Column(name = "tank_lose_game")
    private Integer tankLoseGame;

    @Column(name="deal_rating_point")
    private Integer dealRatingPoint;

    @Column(name = "deal_win_game")
    private Integer dealWinGame;

    @Column(name = "deal_lose_game")
    private Integer dealLoseGame;

    @Column(name="heal_rating_point")
    private Integer healRatingPoint;

    @Column(name = "heal_win_game")
    private Integer healWinGame;

    @Column(name = "heal_lose_game")
    private Integer healLoseGame;

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

    @Id
    @Column(name="is_base")
    private String isBaseData;

    @CreationTimestamp
    @Column(name="rgt_dtm", nullable = true)
    private LocalDateTime rgtDtm;

    @UpdateTimestamp
    @Column(name="udt_dtm", nullable = true)
    private LocalDateTime udtDtm;

    public PlayerForRanking(Long id, Integer playerLevel,
                  Integer tankRatingPoint, Integer dealRatingPoint, Integer healRatingPoint,
                  Integer tankWinGame, Integer tankLoseGame, Integer dealWinGame, Integer dealLoseGame, Integer healWinGame, Integer healLoseGame,
                  Integer winGame, Integer loseGame, Integer drawGame, Long playTime, Long spentOnFire, Integer envKill, String isBaseData) {
        this.id = id ; this.playerLevel = playerLevel;
         this.tankRatingPoint = tankRatingPoint; this.dealRatingPoint = dealRatingPoint;
        this.healRatingPoint = healRatingPoint; this.tankWinGame = tankWinGame; this.tankLoseGame = tankLoseGame;
        this.dealWinGame = dealWinGame; this.dealLoseGame = dealLoseGame; this.healWinGame = healWinGame; this.healLoseGame = healLoseGame;
        this.winGame = winGame; this.loseGame = loseGame; this.drawGame = drawGame; this.playTime = playTime;
        this.spentOnFire = spentOnFire; this.envKill = envKill; this.isBaseData = isBaseData;
    }

    @Override
    public String toString() {
        return "PlayerForRanking : {" +
                "id : " + id +
                " / playerLevel : " + playerLevel +
                " / tankRantingPoint : " + tankRatingPoint +
                " / dealRatingPoint : " + dealRatingPoint +
                " / healRatingPoint : " + healRatingPoint +
                " / winGame : " + winGame +
                " / loseGame : " + loseGame +
                " / drawGame : " + drawGame +
                " / playTime : " + playTime +
                " / spentOnFire : " + spentOnFire +
                " / envKill : " + envKill +
                " / isBaseData : " + isBaseData +
                "}";
    }
}
