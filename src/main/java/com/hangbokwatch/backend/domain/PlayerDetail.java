package com.hangbokwatch.backend.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Getter
@Entity(name = "playerdetail")
@IdClass(PlayerDetailId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlayerDetail {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Id
    @Column(name = "season", nullable = false)
    private Long season;

    @Id
    @Column(name = "hero_order")
    private Integer heroOrder;

    @Column(name = "hero_name")
    private String heroName;

    @Column(name = "hero_name_KR")
    private String heroNameKR;

    @Column(name = "kill_per_death")
    private String killPerDeath;

    @Column(name = "win_rate")
    private String winRate;

    @Column(name = "play_time")
    private String playTime;

    @Column(name = "death_avg")
    private String deathAvg;

    @Column(name = "spent_on_fire_avg")
    private String spentOnFireAvg;

    @Column(name = "heal_per_life")
    private String healPerLife;

    @Column(name = "block_damage_per_life")
    private String blockDamagePerLife;

    @Column(name = "last_hit_per_life")
    private String lastHitPerLife;

    @Column(name = "damage_to_hero_per_life")
    private String damageToHeroPerLife;

    @Column(name = "damage_to_shield_per_life")
    private String damageToShieldPerLife;

    @Column(name = "index1")
    private String index1;

    @Column(name = "index2")
    private String index2;

    @Column(name = "index3")
    private String index3;

    @Column(name = "index4")
    private String index4;

    @Column(name = "index5")
    private String index5;

    @Builder
    public PlayerDetail(Long id, Long season, Integer heroOrder, String heroName, String heroNameKR, String killPerDeath, String winRate,
                  String playTime, String deathAvg, String spentOnFireAvg, String healPerLife, String blockDamagePerLife, String lastHitPerLife, String damageToHeroPerLife,
                  String damageToShieldPerLife, String index1, String index2, String index3, String index4, String index5) {
        this.id = id ;this.season = season; this.heroOrder = heroOrder; this.heroName = heroName; this.heroNameKR = heroNameKR;
        this.killPerDeath = killPerDeath; this.winRate = winRate; this.playTime = playTime; this.deathAvg = deathAvg; this.spentOnFireAvg = spentOnFireAvg;
        this.healPerLife = healPerLife; this.blockDamagePerLife = blockDamagePerLife; this.damageToHeroPerLife = damageToHeroPerLife; this.lastHitPerLife = lastHitPerLife;
        this.damageToShieldPerLife = damageToShieldPerLife; this.index1 = index1; this.index2 = index2; this.index3 = index3; this.index4 = index4; this.index5 = index5;
    }
}
