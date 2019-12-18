package com.hangbokwatch.backend.controller;

import com.hangbokwatch.backend.dto.PlayerDetailDto;
import com.hangbokwatch.backend.dto.PlayerListDto;
import com.hangbokwatch.backend.dto.PlayerSearchDto;
import com.hangbokwatch.backend.service.CrawlingPlayerDataService;
import com.hangbokwatch.backend.service.SearchPlayerListService;
import com.hangbokwatch.backend.service.ShowPlayerDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
public class WebRestController {
    @Autowired
    CrawlingPlayerDataService cpl;
    @Autowired
    SearchPlayerListService spl;
    @Autowired
    ShowPlayerDetailService spd;

    @PostMapping("/showUserList")
    public List<PlayerListDto> getPlayerList(@RequestBody PlayerSearchDto playerDto) {
        System.out.println("WebRestController - getPlayerList 호출됨!");
        String playerName = playerDto.getPlayerName();
        System.out.println("playerName : " + playerName);
        List<PlayerListDto> playerList = cpl.crawlingPlayerList(playerName);
        System.out.println(playerList.size());
        return playerList;
    }

    @PostMapping("/showUserProfile")
    public List<PlayerListDto> getPlayerProfile(@RequestBody List<PlayerListDto> playerList) {
        List<PlayerListDto> resultPlayerList = new ArrayList<PlayerListDto>();
        for(PlayerListDto playerDto : playerList) {
            if("Y".equals(playerDto.getIsPublic())) {
                playerDto = cpl.crawlingPlayerProfile(playerDto);
            }
            System.out.println(playerDto.toString());
            resultPlayerList.add(playerDto);
        }
        System.out.println("15개 조회 끝");
        Collections.sort(resultPlayerList);
        return resultPlayerList;
    }

    @PostMapping("/showPlayerList")
    public List<PlayerListDto> showPlayerList(@RequestBody PlayerSearchDto playerDto) {
        System.out.println("WebRestController - showPlayerList 호출됨!");
        String playerName = playerDto.getPlayerName();
        System.out.println("playerName : " + playerName);
        List<PlayerListDto> playerList = spl.searchPlayerList(playerName);
        System.out.println(playerList.size());
        return playerList;
    }

    @PostMapping("/getDetailData")
    public List<PlayerDetailDto> getDetailData(@RequestBody PlayerSearchDto playerSearchDto) {
        Long id = playerSearchDto.getId();
        List<PlayerDetailDto> playerDetailList = spd.selectPlayerHeroDetail(id);
        return playerDetailList;
    }
}
