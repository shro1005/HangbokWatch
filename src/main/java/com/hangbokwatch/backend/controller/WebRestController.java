package com.hangbokwatch.backend.controller;

import com.hangbokwatch.backend.dto.PlayerDetailDto;
import com.hangbokwatch.backend.dto.PlayerListDto;
import com.hangbokwatch.backend.dto.PlayerSearchDto;
import com.hangbokwatch.backend.dto.TrendlindDto;
import com.hangbokwatch.backend.service.CrawlingPlayerDataService;
import com.hangbokwatch.backend.service.SearchPlayerListService;
import com.hangbokwatch.backend.service.ShowPlayerDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Slf4j
@RestController
public class WebRestController {
    @Autowired
    CrawlingPlayerDataService cpl;
    @Autowired
    SearchPlayerListService spl;
    @Autowired
    ShowPlayerDetailService spd;

    @PostMapping("/showPlayerList")
    public List<PlayerListDto> showPlayerList(@RequestBody PlayerSearchDto playerDto) {
        String playerName = playerDto.getPlayerName();
        log.info("{} >>>>>>>> showPlayerList 호출 | 검색값 : {}", "미로그인 유저", playerName);

        List<PlayerListDto> playerList = spl.searchPlayerList(playerName);

        log.info("{} >>>>>>>> showPlayerList 종료 | {}건의 데이터 DB조회 및 회신", "미로그인 유저", playerList.size());
        log.info("===================================================================");
        return playerList;
    }

    @PostMapping("/crawlingPlayerList")
    public List<PlayerListDto> crawlingPlayerList(@RequestBody PlayerSearchDto playerDto) {
        String playerName = playerDto.getPlayerName();
        log.info("{} >>>>>>>> crawlingPlayerList 호출 | 검색한 값이 DB에 없어 크롤링합니다. 검색값 : {}", "미로그인 유저", playerName);

        List<PlayerListDto> playerList = cpl.crawlingPlayerList(playerName);

        log.info("{} >>>>>>>> crawlingPlayerList 종료 | {}건의 데이터 크롤링 및 회신", "미로그인 유저", playerList.size());
        log.info("===================================================================");
        return playerList;
    }

    @PostMapping("/getDetailData")
    public Map<String, Object> getDetailData(@RequestBody PlayerSearchDto playerSearchDto) {

        Map<String, Object> map = new HashMap<>();
        Long id = playerSearchDto.getId();
        log.info("{} >>>>>>>> getDetailData 호출 | 플레이어의 상세 정보를 조회합니다. 검색값(id) : {}({})", "미로그인 유저", id, playerSearchDto.getBattleTag());
        List<PlayerDetailDto> playerDetailList = spd.selectPlayerHeroDetail(id);
        List<TrendlindDto> trendlindList = spd.selectPlayerTrendline(id);

        map.put("detail", playerDetailList);
        map.put("trendline", trendlindList);

        log.info("{} >>>>>>>> getDetailData 종료 | detail {}건, trendline {}건 회신", "미로그인 유저", playerDetailList.size(), trendlindList.size());
        log.info("===================================================================");
        return map;
    }

    /**현재 미사용 2019.12.12 */
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


}
