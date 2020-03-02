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
    private String killPerDeath;  //1
    private String winRate;
    private String playTime;
    private String deathAvg;     //2
    private String spentOnFireAvg;
    private String healPerLife; //3
    private String blockDamagePerLife; //4
    private String lastHitPerLife;  //5
    private String damageToHeroPerLife;  //6
    private String damageToShieldPerLife;  //7
    private String index1;  //8
    private String index2;  //9
    private String index3;  //10
    private String index4;  //11
    private String index5;  //12
    private String title1;  //13
    private String title2;  //14
    private String title3;  //15
    private String title4;  //16
    private String title5;  //17

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
