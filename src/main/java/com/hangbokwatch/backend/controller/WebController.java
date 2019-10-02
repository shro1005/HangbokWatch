package com.hangbokwatch.backend.controller;

import com.hangbokwatch.backend.dto.PlayerListDto;
import com.hangbokwatch.backend.service.SavePlayerDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * WebController : 간단한 화면 이동 매핑을 담당하는 controller (2019.09.24 최초작성)
 * 2019.09.04 - index(), search() 최초작성
 */
@Controller
public class WebController {
    @Autowired
    SavePlayerDetailService spd;

    @GetMapping("/showPlayerDetail/{forUrl}")
    public String getPlayerDetail(@PathVariable String forUrl) {
        System.out.println("getPlayerDetail -> forUrl : " + forUrl);
        spd.savePlayerExample(forUrl);
        return "playerDetail";
    }
}
