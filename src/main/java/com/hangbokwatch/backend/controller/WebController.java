package com.hangbokwatch.backend.controller;

import com.hangbokwatch.backend.dto.PlayerListDto;
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

    @GetMapping("/showPlayerDetail/{battleTag}")
    public String getPlayerDetail(@PathVariable String battleTag) {
        System.out.println("battleTag : " + battleTag);
        return "playerDetail";
    }
}
