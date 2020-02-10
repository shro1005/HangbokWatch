package com.hangbokwatch.backend.controller;

import com.hangbokwatch.backend.dto.*;
import com.hangbokwatch.backend.dto.auth.SessionUser;
import com.hangbokwatch.backend.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class WebRestController {

    private final CrawlingPlayerDataService cpl;
    private final SearchPlayerListService spl;
    private final ShowPlayerDetailService spd;
    private final ShowUserFavoriteService suf;
    private final GetRankingDataService grd;
    private final HttpSession httpSession;

    @PostMapping("/showPlayerList")
    public List<PlayerListDto> showPlayerList(@RequestBody PlayerSearchDto playerDto) {
        Map<String, Object> sessionItems = sessionCheck();
        String sessionBattleTag = (String) sessionItems.get("sessionBattleTag");

        String playerName = playerDto.getPlayerName();
        log.info("{} >>>>>>>> showPlayerList 호출 | 검색값 : {}", sessionBattleTag, playerName);

        List<PlayerListDto> playerList = spl.searchPlayerList(playerName, sessionItems);

        log.info("{} >>>>>>>> showPlayerList 종료 | {}건의 데이터 DB조회 및 회신", sessionBattleTag, playerList.size());
        log.info("===================================================================");
        return playerList;
    }

    @PostMapping("/crawlingPlayerList")
    public List<PlayerListDto> crawlingPlayerList(@RequestBody PlayerSearchDto playerDto) {
        Map<String, Object> sessionItems = sessionCheck();
        String sessionBattleTag = (String) sessionItems.get("sessionBattleTag");

        String playerName = playerDto.getPlayerName();
        log.info("{} >>>>>>>> crawlingPlayerList 호출 | 검색한 값이 DB에 없어 크롤링합니다. 검색값 : {}", sessionBattleTag, playerName);

        List<PlayerListDto> playerList = cpl.crawlingPlayerList(playerName, sessionItems);

        log.info("{} >>>>>>>> crawlingPlayerList 종료 | {}건의 데이터 크롤링 및 회신", sessionBattleTag, playerList.size());
        log.info("===================================================================");
        return playerList;
    }

    @PostMapping("/getDetailData")
    public Map<String, Object> getDetailData(@RequestBody PlayerSearchDto playerSearchDto) {
        Map<String, Object> sessionItems = sessionCheck();
        String sessionBattleTag = (String) sessionItems.get("sessionBattleTag");

        Map<String, Object> map = new HashMap<>();
        Long id = playerSearchDto.getId();
        log.info("{} >>>>>>>> getDetailData 호출 | 플레이어의 상세 정보를 조회합니다. 검색값(id) : {}({})", sessionBattleTag, id, playerSearchDto.getBattleTag());
        List<PlayerDetailDto> playerDetailList = spd.selectPlayerHeroDetail(id, sessionItems);
        List<TrendlindDto> trendlindList = spd.selectPlayerTrendline(id, sessionItems);

        map.put("detail", playerDetailList);
        map.put("trendline", trendlindList);

        log.info("{} >>>>>>>> getDetailData 종료 | detail {}건, trendline {}건 회신", sessionBattleTag, playerDetailList.size(), trendlindList.size());
        log.info("===================================================================");
        return map;
    }

    @PostMapping("/refreshFavorite")
    public String refreshFavorite(@RequestBody PlayerSearchDto favorite) {
        Map<String, Object> sessionItems = sessionCheck();
        String sessionBattleTag = (String) sessionItems.get("sessionBattleTag");

        Long clickedId = favorite.getId();
        String likeOrNot = favorite.getPlayerName();

        log.info("{} >>>>>>>> refreshFavorite 호출 | 즐겨찾기 버튼 클릭. 클릭한 유저 ID : {}, 좋아요 : {}", sessionBattleTag, clickedId, likeOrNot);

        spd.refreshFavorite(clickedId, likeOrNot, sessionItems);

        log.info("{} >>>>>>>> refreshFavorite 호출 | 즐겨찾기 버튼 클릭 작업 완료", sessionBattleTag);
        log.info("===================================================================");
        return "Success";
    }

    @PostMapping("/getFavoriteData")
    public Map<String, Object> getFavoriteData() {
        Map<String, Object> sessionItems = sessionCheck();
        String sessionBattleTag = (String) sessionItems.get("sessionBattleTag");

        log.info("{} >>>>>>>> getFavoriteData 호출 | 즐겨찾기 플레이어 리스트 조회", sessionBattleTag);

        List<PlayerListDto> myFavoritePlayerList = suf.findMyFavoritePlayerList(sessionItems);
        List<PlayerListDto> favoritingMePlayerList = suf.findFavoritingMePlayerList(sessionItems);

        Map<String, Object> map = new HashMap<>();
        map.put("myFavorite", myFavoritePlayerList);
        map.put("favoritingMe", favoritingMePlayerList);

        log.info("{} >>>>>>>> getFavoriteData 호출 | 즐겨찾기 플레이어 리스트 조회 완료", sessionBattleTag);
        log.info("===================================================================");

        return map;
    }

    @PostMapping("/getRankingData")
    public Map<String, Object> getRankingData() {
        Map<String, Object> sessionItems = sessionCheck();
        String sessionBattleTag = (String) sessionItems.get("sessionBattleTag");

        log.info("{} >>>>>>>> getRankingData 호출 | 랭킹 데이터 추출", sessionBattleTag);

        List<PlayerRankingDto> tankRatingRankingList = grd.getTankRatingRankingData(sessionItems);
        List<PlayerRankingDto> dealRatingRankingList = grd.getDealRatingRankingData(sessionItems);
        List<PlayerRankingDto> healRatingRankingList = grd.getHealRatingRankingData(sessionItems);
        List<PlayerRankingDto> playTimeRankingList = grd.getPlayTimeRankingData(sessionItems);
        List<PlayerRankingDto> spentOnFireRankingList = grd.getSpentOnFireRankingData(sessionItems);
        List<PlayerRankingDto> envKillRankingList = grd.getEnvKillRankingData(sessionItems);

        Map<String, Object> map = new HashMap<>();
        map.put("tankRating", tankRatingRankingList);
        map.put("dealRating", dealRatingRankingList);
        map.put("healRating", healRatingRankingList);
        map.put("playTime", playTimeRankingList);
        map.put("spentOnFire", spentOnFireRankingList);
        map.put("envKill", envKillRankingList);

        log.info("{} >>>>>>>> getRankingData 호출 | 랭킹 데이터 추출 완료", sessionBattleTag);
        log.info("===================================================================");

        return map;
    }

    @PostMapping("/getRankingData")
    public Map<String, Object> getRankerData() {
        Map<String, Object> sessionItems = sessionCheck();
        String sessionBattleTag = (String) sessionItems.get("sessionBattleTag");

        log.info("{} >>>>>>>> getRankerData 호출 | 랭커 데이터 추출", sessionBattleTag);
        List<PlayerListDto> tankRankerList = grd.getTankRankergData(sessionItems);
        List<PlayerListDto> dealRankerList = grd.getDealRankerData(sessionItems);
        List<PlayerListDto> healRankerList = grd.getHealRankerData(sessionItems);
        List<PlayerListDto> totalAvgRankerList = grd.getTotalAvgRankerData(sessionItems);

        Map<String, Object> map = new HashMap<>();

        map.put("tankRanker", tankRankerList);
        map.put("dealRanker", dealRankerList);
        map.put("healRanker", healRankerList);
        map.put("totalAvgRanker", totalAvgRankerList);

        log.info("{} >>>>>>>> getRankerData 호출 | 랭커 데이터 추출 완료", sessionBattleTag);
        log.info("===================================================================");

        return map;
    }

    private Map<String, Object> sessionCheck() {
        // CustomOAuth2UserService에서 로그인 성공시 세션에 SessionUser를 저장하도록 구성했으므로
        // 세션에서 httpSession.getAttribute("user")를 통해 User 객체를 가져올 수 있다.
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        String battleTag = "미로그인 유저";
        if (user != null) {
            battleTag = user.getBattleTag();
        }

        Map<String, Object> map = new HashMap<>();
        map.put("loginUser", user);
        map.put("sessionBattleTag", battleTag);
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
