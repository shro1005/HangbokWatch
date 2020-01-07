package com.hangbokwatch.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrendlindDto {
    private Long id;
    private String udtDtm;
    private Integer tankRatingPoint;
    private Integer dealRatingPoint;
    private Integer healRatingPoint;
    private Integer tankWinGame;
    private Integer tankLoseGame;
    private Integer dealWinGame;
    private Integer dealLoseGame;
    private Integer healWinGame;
    private Integer healLoseGame;

    @Override
    public String toString() {
        return "Ana{" +
                "id=" + id +
                ", udtDtm=" + udtDtm +
                ", tankRatingPoint=" + tankRatingPoint +
                ", dealRatingPoint='" + dealRatingPoint + '\'' +
                ", healRatingPoint='" + healRatingPoint + '\'' +
                ", tankWinGame='" + tankWinGame + '\'' +
                ", tankLoseGame='" + tankLoseGame + '\'' +
                ", dealWinGame='" + dealWinGame + '\'' +
                ", dealLoseGame='" + dealLoseGame + '\'' +
                ", healWinGame='" + healWinGame + '\'' +
                ", healLoseGame='" + healLoseGame + '\'' +
                '}';
    }
}
