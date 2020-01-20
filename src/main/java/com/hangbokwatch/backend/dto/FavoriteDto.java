package com.hangbokwatch.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteDto {

    private Long id;
    private Long clickedId;
    private String likeOrNot;
}
