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

    @Column(name="player_level", nullable = false)
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
}
