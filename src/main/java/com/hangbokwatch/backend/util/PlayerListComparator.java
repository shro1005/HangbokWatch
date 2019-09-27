package com.hangbokwatch.backend.util;

import com.hangbokwatch.backend.dto.PlayerListDto;

import java.util.Comparator;

public class PlayerListComparator implements Comparator<PlayerListDto> {

    @Override
    public int compare(PlayerListDto o1, PlayerListDto o2) {
        if("Y".equals(o1.getIsPublic()) && "N".equals(o2.getIsPublic()) ) {
            return 1;
        }else if("N".equals(o1.getIsPublic()) && "Y".equals(o2.getIsPublic())) {
            return -1;
        }else {
            if (o1.getPlayerLevel() - o2.getPlayerLevel() != 0) {
                return o1.getPlayerLevel() - o2.getPlayerLevel();
            }else {
                return (o1.getTankRatingPoint() + o1.getDealRatingPoint() + o1.getHealRatingPoint())/3 - (o2.getTankRatingPoint() + o2.getDealRatingPoint() + o2.getHealRatingPoint())/3;
            }
        }
    }
}

