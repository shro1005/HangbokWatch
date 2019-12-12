package com.hangbokwatch.backend.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity(name="moira")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Moira {
    /**공통 데이터*/
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "win_game")
    private Integer winGame;

    @Column(name = "lose_game")
    private Integer loseGame;

    //    @Column(name = "draw_game")
//    private Integer drawGame;
//
    @Column(name = "entire_game")
    private Integer entireGame;

    @Column(name = "win_rate")
    private String winRate;

    @Column(name = "play_time")
    private String playTime;

    @Column(name = "kill_per_death")
    private String killPerDeath;

//    @Column(name = "deaths")
//    private Long deaths;

    @Column(name = "spent_on_fire_avg")
    private String spentOnFireAvg;

    @Column(name = "death_avg")
    private String deathAvg;

    @Column(name = "heal_per_life")
    private String healPerLife;

    @Column(name = "damage_to_hero_per_life")
    private String damageToHeroPerLife;

    /**영웅별 특수 데이터*/
    @Column(name = "coalescence_kill_avg")
    private String coalescenceKillAvg;

    @Column(name = "coalescence_heal_avg")
    private String coalescenceHealAvg;

    @Column(name = "selfheal_per_life")
    private String selfHealPerLife;

    /**메달 데이터*/
    @Column(name = "gold_medal")
    private String goldMedal;

    @Column(name = "silver_medal")
    private String silverMedal;

    @Column(name = "bronze_medal")
    private String bronzeMedal;

    @Override
    public String toString() {
        return "Moira{" +
                "id=" + id +
                ", winGame=" + winGame +
                ", loseGame=" + loseGame +
                ", winRate='" + winRate + '\'' +
                ", playTime='" + playTime + '\'' +
                ", killPerDeath='" + killPerDeath + '\'' +
                ", spentOnFireAvg='" + spentOnFireAvg + '\'' +
                ", deathAvg='" + deathAvg + '\'' +
                ", healPerLife='" + healPerLife + '\'' +
                ", damageToHero='" + damageToHeroPerLife + '\'' +
                ", coalescenceKillAvg='" + coalescenceKillAvg + '\'' +
                ", coalescenceHealAvg='" + coalescenceHealAvg + '\'' +
                ", selfhealPerLife='" + selfHealPerLife + '\'' +
                ", goldMedal='" + goldMedal + '\'' +
                ", silverMedal='" + silverMedal + '\'' +
                ", bronzeMedal='" + bronzeMedal + '\'' +
                '}';
    }
}
