package com.hangbokwatch.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShowRankingService {

    private final HttpSession httpSession;

    public void showOurRanking(Map<String, Object> sessionItems) {
        String sessionBattleTag = (String) sessionItems.get("sessionBattleTag");

        log.info("{} >>>>>>>> showOurRanking 호출 | 우리만의 랭킹 조회 ", sessionBattleTag);


    }
}
