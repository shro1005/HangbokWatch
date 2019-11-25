package com.hangbokwatch.backend.service;

import com.hangbokwatch.backend.dao.PlayerRepository;
import com.hangbokwatch.backend.domain.Player;
import com.hangbokwatch.backend.dto.PlayerListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchPlayerListService {
    @Autowired
    PlayerRepository playerRepository;

    public List<PlayerListDto> searchPlayerList(String playerName) {
        List<PlayerListDto> playerListDtos = new ArrayList<PlayerListDto>();
        List<Player> searchResult = new ArrayList<Player>();
        if(playerName.indexOf("#") == -1) {  // 검색 유형이 배틀태그가 아닌경우 (그냥 유저명)
            playerName = playerName.toUpperCase();
            searchResult = playerRepository.findByPlayerNameIgnoreCase(playerName);
        }else{
            searchResult = playerRepository.findByBattleTag(playerName);
        }

        for (Player player : searchResult) {
            PlayerListDto playerListDto = new PlayerListDto(player.getId(), player.getBattleTag(), player.getPlayerName(), player.getForUrl(), player.getPlayerLevel()
            , player.getIsPublic(), player.getPlatform(), player.getPortrait(), player.getTankRatingPoint(), player.getDealRatingPoint(), player.getHealRatingPoint()
            , player.getWinGame(), player.getLoseGame(), player.getDrawGame(), "", player.getMostHero1(), player.getMostHero2(), player.getMostHero3());

            playerListDtos.add(playerListDto);
        }

        return playerListDtos;
    }
}
