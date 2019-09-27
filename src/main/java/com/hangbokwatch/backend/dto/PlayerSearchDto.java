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

    @Builder
    public  PlayerSearchDto(String playerName) {
        this.playerName = playerName;
    }
}
