package com.hangbokwatch.backend.controller;

import com.hangbokwatch.backend.dto.PlayerDetailDto;
import com.hangbokwatch.backend.dto.PlayerListDto;
import com.hangbokwatch.backend.dto.PlayerSearchDto;
import com.hangbokwatch.backend.dto.TrendlindDto;
import com.hangbokwatch.backend.service.CrawlingPlayerDataService;
import com.hangbokwatch.backend.service.SearchPlayerListService;
import com.hangbokwatch.backend.service.ShowPlayerDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class WebRestController {
    @Autowired
    CrawlingPlayerDataService cpl;
    @Autowired
    SearchPlayerListService spl;
    @Autowired
    ShowPlayerDetailService spd;

    @PostMapping("/showUserList")
    public List<PlayerListDto> showUserList(@RequestBody PlayerSearchDto playerDto) {
        System.out.println("WebRestController - showUserList 호출됨!");
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

//    @PostMapping("/getDetailData")
//    public List<PlayerDetailDto> getDetailData(@RequestBody PlayerSearchDto playerSearchDto) {
//        System.out.println("WebRestController - getDetailData 호출됨!");
//        Long id = playerSearchDto.getId();
//        List<PlayerDetailDto> playerDetailList = spd.selectPlayerHeroDetail(id);
//        List<TrendlindDto> trendlindList = spd.selectPlayerTrendline(id);
//
//        return playerDetailList;
//    }

    @PostMapping("/getDetailData")
    public Map<String, Object> getDetailData(@RequestBody PlayerSearchDto playerSearchDto) {
        System.out.println("WebRestController - getDetailData 호출됨!");
        Map<String, Object> map = new HashMap<>();
        Long id = playerSearchDto.getId();
        List<PlayerDetailDto> playerDetailList = spd.selectPlayerHeroDetail(id);
        List<TrendlindDto> trendlindList = spd.selectPlayerTrendline(id);

        map.put("detail", playerDetailList);
        map.put("trendline", trendlindList);

        return map;
    }
}
