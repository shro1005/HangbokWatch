package com.hangbokwatch.backend.service;

import com.hangbokwatch.backend.dao.PlayerRepository;
import com.hangbokwatch.backend.dto.PlayerListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchPlayerListService {
    @Autowired
    PlayerRepository playerRepository;

    Boolean isBattletag = true;

    public List<PlayerListDto> searchPlayerList(String playerName) {
        if(playerName.indexOf("#") == -1) {  // 검색 유형이 배틀태그가 아닌경우 (그냥 유저명)
            isBattletag = false;
        }



        return null;
    }
}
