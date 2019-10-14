package com.hangbokwatch.backend.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity(name="dva")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dva {
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
//    @Column(name = "entire_game")
//    private Integer entireGame;

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

    @Column(name = "block_damage_per_life")
    private String blockDamage;

    @Column(name = "damage_to_hero_per_life")
    private String damageToHero;

    @Column(name = "damage_to_shield_per_life")
    private String damageToShield;

    @Column(name = "mecha_suicide_kill_avg")
    private String mechaSuicideKillAvg;

    @Column(name = "mecah_call_avg")
    private String mechaCallAvg;

    @Column(name = "gold_medal")
    private String goldMedal;

    @Column(name = "silver_medal")
    private String silverMedal;

    @Column(name = "bronze_medal")
    private String bronzeMedal;

    @Override
    public String toString() {
        return "Dva{" +
                "id=" + id +
                ", winGame=" + winGame +
                ", loseGame=" + loseGame +
                ", winRate='" + winRate + '\'' +
                ", playTime='" + playTime + '\'' +
                ", killPerDeath='" + killPerDeath + '\'' +
                ", spentOnFireAvg='" + spentOnFireAvg + '\'' +
                ", deathAvg='" + deathAvg + '\'' +
                ", blockDamage='" + blockDamage + '\'' +
                ", damageToHero='" + damageToHero + '\'' +
                ", damageToShield='" + damageToShield + '\'' +
                ", mechaSuicideKillAvg='" + mechaSuicideKillAvg + '\'' +
                ", mechaCallAvg='" + mechaCallAvg + '\'' +
                ", goldMedal='" + goldMedal + '\'' +
                ", silverMedal='" + silverMedal + '\'' +
                ", bronzeMedal='" + bronzeMedal + '\'' +
                '}';
    }
}
