package com.hangbokwatch.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * WebController : 간단한 화면 이동 매핑을 담당하는 controller (2019.09.24 최초작성)
 * 2019.09.04 - index(), search() 최초작성
 */
@Controller
public class WebController {

    /* 초기 값 화면 세팅 index -> search */
    @GetMapping("/index")
    public String index(Model model) {
        return "search";
    }

    /* search 매핑 */
    @GetMapping("/search")
    public String search(Model model) {
        return "search";
    }
}
