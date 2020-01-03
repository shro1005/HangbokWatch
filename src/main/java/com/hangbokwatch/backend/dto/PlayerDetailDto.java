package com.hangbokwatch.backend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDetailDto {
    private Long id;
    private Long season;
    private Integer order;
    private String heroName;
    private String heroNameKR;
    private String killPerDeath;
    private String winRate;
    private String playTime;
    private String deathAvg;
    private String spentOnFireAvg;
    private String healPerLife;
    private String blockDamagePerLife;
    private String lastHitPerLife;
    private String damageToHeroPerLife;
    private String damageToShieldPerLife;
    private String index1;
    private String index2;
    private String index3;
    private String index4;
    private String index5;
    private String title1;
    private String title2;
    private String title3;
    private String title4;
    private String title5;

    public String toString() {
        return "Detail{" +
                "id=" + id +
                ", season=" + season +
                ", order=" + order +
                ", heroName='" + heroName + '\'' +
                ", heroNameKR='" + heroNameKR + '\'' +
                ", winRate='" + winRate + '\'' +
                ", playTime='" + playTime + '\'' +
                ", deathAvg='" + deathAvg + '\'' +
                ", spentOnFireAvg='" + spentOnFireAvg + '\'' +
                ", healPerLife='" + healPerLife + '\'' +
                ", blockDamagePerLife='" + blockDamagePerLife + '\'' +
                ", lastHitPerLife='" + lastHitPerLife + '\'' +
                ", damageToHeroPerLife='" + damageToHeroPerLife + '\'' +
                ", damageToShieldPerLife='" + damageToShieldPerLife + '\'' +
                ", index1='" + index1 + '\'' +
                ", index2='" + index2 + '\'' +
                ", index3='" + index3 + '\'' +
                ", index4='" + index4 + '\'' +
                ", index5='" + index5 + '\'' +
                ", index1='" + title1 + '\'' +
                ", index2='" + title2 + '\'' +
                ", index3='" + title3 + '\'' +
                ", index4='" + title4 + '\'' +
                ", index5='" + title5 + '\'' +
                '}';
    }

}
