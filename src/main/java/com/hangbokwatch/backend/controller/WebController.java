package com.hangbokwatch.backend.controller;

import com.hangbokwatch.backend.dto.CompetitiveDetailDto;
import com.hangbokwatch.backend.service.ShowPlayerDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * WebController : 간단한 화면 이동 매핑을 담당하는 controller (2019.09.24 최초작성)
 * 2019.09.04 - index(), search() 최초작성
 */
@Controller
public class WebController {
    @Autowired
    ShowPlayerDetailService spd;

    @GetMapping("/showPlayerDetail/{forUrl}")
    public String getPlayerDetail(@PathVariable String forUrl, Model model) {
        System.out.println("getPlayerDetail -> forUrl : " + forUrl);
        CompetitiveDetailDto cdDto = spd.showPlayerExample(forUrl);


        model.addAttribute("player", cdDto.getPlayer());
        model.addAttribute("dva", cdDto.getDva());
        model.addAttribute("reinhardt", cdDto.getReinhardt());
        model.addAttribute("roadHog", cdDto.getRoadHog());
        model.addAttribute("orisa", cdDto.getOrisa());
        model.addAttribute("winston", cdDto.getWinston());
        model.addAttribute("zarya", cdDto.getZarya());
        model.addAttribute("sigma", cdDto.getSigma());

        return "playerDetail";
    }
}
