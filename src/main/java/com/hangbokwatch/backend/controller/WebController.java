package com.hangbokwatch.backend.controller;

import com.hangbokwatch.backend.dto.CompetitiveDetailDto;
import com.hangbokwatch.backend.dto.PlayerListDto;
import com.hangbokwatch.backend.service.ShowPlayerDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * WebController : 간단한 화면 이동 매핑을 담당하는 controller (2019.09.24 최초작성)
 * 2019.09.04 - index(), search() 최초작성
 */
@Slf4j
@Controller
public class WebController {
    @Autowired
    ShowPlayerDetailService spd;

    @GetMapping("/")
    public String goToIndexView() {
        return "index";
    }

    @GetMapping("/showPlayerDetail/{forUrl}")
    public String showPlayerDetail(@PathVariable String forUrl, Model model) {
        log.info("{} | showPlayerDetail 호출 | 조회 url : {}", "미로그인 유저", forUrl);
        CompetitiveDetailDto cdDto = spd.showPlayerDetailService(forUrl);

        if(cdDto.getPlayer() != null) {
            String battleTag = cdDto.getPlayer().getBattleTag();
            String tag = battleTag.substring(battleTag.indexOf("#"));

            model.addAttribute("tag", tag);
            model.addAttribute("player", cdDto.getPlayer());
            model.addAttribute("playerDetails", cdDto.getPlayerDetailList());
            model.addAttribute("count", cdDto.getCount());
        }

        log.info("{} | showPlayerDetail 종료 | playerDetail.html 화면 이동", "미로그인 유저");
        log.info("===================================================================");
        return "playerDetail";
    }

    @GetMapping("/showPlayerListFromDetail/{userInput}")
    public String showPlayerListFromDetail(@PathVariable String userInput, Model model) {
        log.info("{} | showPlayerListFromDetail 호출 | 검색값 : {}", "미로그인 유저", userInput);
        if(userInput.indexOf("-") != -1) {
            userInput = userInput.replace("-", "#");
        }
        model.addAttribute("isFromDetail", "Y");
        model.addAttribute("userInput", userInput);
        log.info("{} | showPlayerListFromDetail 종료 | index.html 화면 이동", "미로그인 유저");
        log.info("===================================================================");
        return "index";
    }

    @GetMapping("/refreshPlayerDetail/{forUrl}")
    public String refreshPlayerDetail(@PathVariable String forUrl, Model model) {
        log.info("{} | refreshPlayerDetail 호출 | 조회 url : {}", "미로그인 유저", forUrl);
        CompetitiveDetailDto cdDto = spd.refreshPlayerDetail(forUrl);

        if(cdDto.getPlayer() != null) {
            String battleTag = cdDto.getPlayer().getBattleTag();
            String tag = battleTag.substring(battleTag.indexOf("#"));

            model.addAttribute("tag", tag);
            model.addAttribute("player", cdDto.getPlayer());
            model.addAttribute("playerDetails", cdDto.getPlayerDetailList());
        }
        log.info("{} | refreshPlayerDetail 종료 | playerDetail.html 화면 이동", "미로그인 유저");
        log.info("===================================================================");
        return "playerDetail";
    }
}
