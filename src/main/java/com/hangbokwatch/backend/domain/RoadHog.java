package com.hangbokwatch.backend.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity(name="roadhog")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoadHog {
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

    @Column(name = "solo_kill_avg")
    private String soloKillAvg;

    @Column(name = "damage_to_hero_per_life")
    private String damageToHeroPerLife;

    @Column(name = "damage_to_shield_per_life")
    private String damageToShieldPerLife;

    /**영웅별 특수 데이터*/
    @Column(name = "whole_hog_kill_avg")
    private String wholeHogKillAvg;

    @Column(name = "chain_hook_accuracy")
    private String chainHookAccuracy;

    @Column(name = "hooking_enemy_avg")
    private String hookingEnemyAvg;

    @Column(name = "self_heal_per_life")
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
        return "Roadhog{" +
                "id=" + id +
                ", winGame=" + winGame +
                ", loseGame=" + loseGame +
                ", winRate='" + winRate + '\'' +
                ", playTime='" + playTime + '\'' +
                ", killPerDeath='" + killPerDeath + '\'' +
                ", spentOnFireAvg='" + spentOnFireAvg + '\'' +
                ", deathAvg='" + deathAvg + '\'' +
                ", soloKillAvg='" + soloKillAvg + '\'' +
                ", damageToHeroPerLife='" + damageToHeroPerLife + '\'' +
                ", damageToShieldPerLife='" + damageToShieldPerLife + '\'' +
                ", wholeHogKillAvg='" + wholeHogKillAvg + '\'' +
                ", chainHookAccuracy='" + chainHookAccuracy + '\'' +
                ", hookingEnemyAvg='" + hookingEnemyAvg + '\'' +
                ", selfHealPerLife='" + selfHealPerLife + '\'' +
                ", goldMedal='" + goldMedal + '\'' +
                ", silverMedal='" + silverMedal + '\'' +
                ", bronzeMedal='" + bronzeMedal + '\'' +
                '}';
    }
}
