package com.hangbokwatch.backend.controller;

import com.hangbokwatch.backend.dto.CompetitiveDetailDto;
import com.hangbokwatch.backend.dto.PlayerListDto;
import com.hangbokwatch.backend.service.ShowPlayerDetailService;
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
@Controller
public class WebController {
    @Autowired
    ShowPlayerDetailService spd;

    @GetMapping("/")
    public String goToIndexView() {
        return "index";
    }

    @GetMapping("/showPlayerDetail/{forUrl}")
    public String getPlayerDetail(@PathVariable String forUrl, Model model) {
        System.out.println("getPlayerDetail -> forUrl : " + forUrl);
        CompetitiveDetailDto cdDto = spd.showPlayerExample(forUrl);

        if(cdDto.getPlayer() != null) {
            String battleTag = cdDto.getPlayer().getBattleTag();
            String tag = battleTag.substring(battleTag.indexOf("#"));

            model.addAttribute("tag", tag);
            model.addAttribute("player", cdDto.getPlayer());
            model.addAttribute("playerDetails", cdDto.getPlayerDetailList());
            model.addAttribute("count", cdDto.getCount());
        }
        //        return "playerDetail-test";
        return "playerDetail";
    }

    @GetMapping("/showPlayerListFromDetail/{userInput}")
    public String getPlayerList(@PathVariable String userInput, Model model) {
        if(userInput.indexOf("-") != -1) {
            userInput = userInput.replace("-", "#");
        }
        model.addAttribute("isFromDetail", "Y");
        model.addAttribute("userInput", userInput);
        return "index";
    }

    @GetMapping("/refreshPlayerDetail/{forUrl}")
    public String refreshPlayerDetail(@PathVariable String forUrl, Model model) {
        System.out.println("getPlayerDetail -> forUrl : " + forUrl);
        CompetitiveDetailDto cdDto = spd.refreshPlayerDetail(forUrl);

        if(cdDto.getPlayer() != null) {
            String battleTag = cdDto.getPlayer().getBattleTag();
            String tag = battleTag.substring(battleTag.indexOf("#"));

            model.addAttribute("tag", tag);
            model.addAttribute("player", cdDto.getPlayer());
            model.addAttribute("playerDetails", cdDto.getPlayerDetailList());
        }
//        return "playerDetail-test";
        return "playerDetail";
    }
}
