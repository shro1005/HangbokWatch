package com.hangbokwatch.backend.controller;

import com.hangbokwatch.backend.dto.PlayerListDto;
import com.hangbokwatch.backend.dto.PlayerSearchDto;
import com.hangbokwatch.backend.service.CrawlingPlayerListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WebRestController {
    @Autowired
    CrawlingPlayerListService cpl;

    @PostMapping("/showUserList")
    public List<PlayerListDto> getPlayerList(@RequestBody PlayerSearchDto playerDto) {
        System.out.println("WebRestController - getPlayerList 호출됨!");
        String playerName = playerDto.getPlayerName();
        System.out.println("playerName : " + playerName);
        List<PlayerListDto> playerList = cpl.crawling(playerName);
        return playerList;
    }

}
