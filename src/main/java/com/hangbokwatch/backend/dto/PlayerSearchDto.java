package com.hangbokwatch.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlayerSearchDto {
    private String playerName;
    private String forUrl;
    private String isPublic;
    private String battleTag;
    private String portrait;
    private Integer playerLevel;
    private String platform;
    private Long id;

    @Builder
    public  PlayerSearchDto(String playerName) {
        this.playerName = playerName;
    }

    @Builder
    public PlayerSearchDto(Long id) {this.id =id;}

    @Builder
    public PlayerSearchDto(String battleTag, String forUrl, String isPublic, String portrait, String platform, Integer playerLevel) {
        this.battleTag = battleTag; this.forUrl = forUrl; this.isPublic = isPublic; this.portrait = portrait;
        this.platform = platform; this.playerLevel = playerLevel;
    }
}
