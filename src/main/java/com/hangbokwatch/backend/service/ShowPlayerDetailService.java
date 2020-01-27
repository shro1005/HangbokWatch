package com.hangbokwatch.backend.service;

import com.hangbokwatch.backend.dao.*;
import com.hangbokwatch.backend.dao.player.PlayerDetailRepository;
import com.hangbokwatch.backend.dao.player.PlayerRepository;
import com.hangbokwatch.backend.dao.player.TrendlineRepository;
import com.hangbokwatch.backend.dao.user.FavoriteRepository;
import com.hangbokwatch.backend.domain.player.Player;
import com.hangbokwatch.backend.domain.player.PlayerDetail;
import com.hangbokwatch.backend.domain.player.Trendline;
import com.hangbokwatch.backend.domain.user.Favorite;
import com.hangbokwatch.backend.dto.*;
import com.hangbokwatch.backend.dto.auth.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShowPlayerDetailService {
    @Autowired PlayerRepository playerRepository;
    @Autowired PlayerDetailRepository playerDetailRepository;
    @Autowired SeasonRepository seasonRepository;
    @Autowired TrendlineRepository trendlineRepository;
    @Autowired FavoriteRepository favoriteRepository;

    private final CrawlingPlayerDataService cpd;

    private final HttpSession httpSession;

    public CompetitiveDetailDto showPlayerDetailService(String forUrl, Map<String, Object> sessionItems) {
        String sessionBattleTag = (String) sessionItems.get("sessionBattleTag");

        log.info("{} >>>>>>>> showPlayerDetailService 호출 | 조회 url : {}", sessionBattleTag, forUrl);
        CompetitiveDetailDto cdDto = new CompetitiveDetailDto();
        StopWatch stopWatch = new StopWatch();

        if(forUrl.indexOf("-") != -1 ) {
            forUrl = forUrl.replace("-", "#");

            /** 기존에 없는 플레이어 조회시 */
            if(playerRepository.findByBattleTag(forUrl).size() == 0) {
                log.debug("{} >>>>>>>> showPlayerDetailService 진행중 | {} : player DB에 없는 플레이어이므로 크롤링 시작", sessionBattleTag, forUrl);
                stopWatch.start("크롤링을 통한 플레이어 기본 정보 추출");

                List<PlayerListDto> playerListDtos = cpd.crawlingPlayerList(forUrl, sessionItems);

                stopWatch.stop();

                for (PlayerListDto playerListDto : playerListDtos) {
                    if ("Y".equals(playerListDto.getIsPublic())) {
                        stopWatch.start("경쟁전 디테일 크롤링 및 데이터 저장 까지 총 시간");
                        cdDto = cpd.crawlingPlayerDetail(playerListDto, cdDto, sessionItems);
                        stopWatch.stop();
                        log.debug("{} >>>>>>>> showPlayerDetailService 진행중 | {} : {}", sessionBattleTag, forUrl, stopWatch.prettyPrint());
                    }
                }

            /** 기존에 데이터 등록했던 플레이어 조회시*/
            }else {
                log.debug("{} >>>>>>>> showPlayerDetailService 진행중 | {} : player DB에서 조회 성공", sessionBattleTag, forUrl);
                Player player = playerRepository.findPlayerByBattleTag(forUrl);

                //로그인한 유저인지 확인하여 즐겨찾기를 조회
                SessionUser sessionUser = (SessionUser)sessionItems.get("loginUser");

                String favorite = "N";
                if(sessionUser == null) {
                    //로그인 하지 않은 유저인 경우 session을 검색
                    log.debug("{} >>>>>>>> showPlayerDetailService 진행중 | 세션에 {} 즐겨찾기 존재여부 확인 및 처리", sessionBattleTag, forUrl);
                    Map<Long, FavoriteDto> favoriteList = (Map<Long, FavoriteDto>) httpSession.getAttribute("favoriteList");
                    if (favoriteList != null) {
                        if(favoriteList.get(player.getId()) != null) {
                            favorite = favoriteList.get(player.getId()).getLikeOrNot();
                        }
                    }
                }else {
                    log.debug("{} >>>>>>>> showPlayerDetailService 진행중 | DB에 {} 즐겨찾기 존재여부 확인 및 처리", sessionBattleTag, forUrl);
                    try {
                        favorite = favoriteRepository.findFavoriteByIdAndClickedId(sessionUser.getId(), player.getId()).getLikeornot();
                    }catch (NullPointerException e) {
                        favorite = "N";
                    }
                }

                cdDto.setPlayer(player);
                cdDto.setFavorite(favorite);
            }
        }

        log.info("{} >>>>>>>> showPlayerDetailService 종료 | 조회 url : {}", sessionBattleTag, forUrl);

        return cdDto;
    }

    public CompetitiveDetailDto refreshPlayerDetail(String forUrl, Map<String, Object> sessionItems) {
        String sessionBattleTag = (String) sessionItems.get("sessionBattleTag");

        log.info("{} >>>>>>>> refreshPlayerDetail 호출 | 조회 url : {}", sessionBattleTag, forUrl);
        CompetitiveDetailDto cdDto = new CompetitiveDetailDto();
        StopWatch stopWatch = new StopWatch();

        if(forUrl.indexOf("-") != -1 ) {
            forUrl = forUrl.replace("-", "#");

            stopWatch.start("크롤링을 통한 플레이어 기본 정보 추출");
            log.debug("{} >>>>>>>> refreshPlayerDetail 진행중 | {} : 새로고침 크롤링 시작", sessionBattleTag, forUrl);
            List<PlayerListDto> playerListDtos = cpd.crawlingPlayerList(forUrl, sessionItems);
            stopWatch.stop();

            for (PlayerListDto playerListDto : playerListDtos) {
                if ("Y".equals(playerListDto.getIsPublic())) {
                    stopWatch.start("경쟁전 디테일 크롤링 및 데이터 저장 까지 총 시간");
                    cdDto = cpd.crawlingPlayerDetail(playerListDto, cdDto, sessionItems);
                    stopWatch.stop();
                    log.debug("{} >>>>>>>> refreshPlayerDetail 진행중 | {} : {}", sessionBattleTag, forUrl, stopWatch.prettyPrint());

                    //로그인한 유저인지 확인하여 즐겨찾기를 조회
                    SessionUser sessionUser = (SessionUser)sessionItems.get("loginUser");

                    String favorite = "N";
                    if(sessionUser == null) {
                        //로그인 하지 않은 유저인 경우 session을 검색
                        log.debug("{} >>>>>>>> refreshPlayerDetail 진행중 | 세션에 {} 즐겨찾기 존재여부 확인 및 처리", sessionBattleTag, forUrl);
                        Map<Long, FavoriteDto> favoriteList = (Map<Long, FavoriteDto>) httpSession.getAttribute("favoriteList");
                        if (favoriteList != null) {
                            if(favoriteList.get(playerListDto.getId()) != null) {
                                favorite = favoriteList.get(playerListDto.getId()).getLikeOrNot();
                            }
                        }
                    }else {
                        log.debug("{} >>>>>>>> refreshPlayerDetail 진행중 | DB에 {} 즐겨찾기 존재여부 확인 및 처리", sessionBattleTag, forUrl);
                        try {
                            favorite = favoriteRepository.findFavoriteByIdAndClickedId(sessionUser.getId(), playerListDto.getId()).getLikeornot();
                        }catch (NullPointerException e) {
                            favorite = "N";
                        }
                    }
                    cdDto.setFavorite(favorite);
                }
            }
        }

        log.info("{} >>>>>>>> refreshPlayerDetail 종료 | 조회 url : {}", sessionBattleTag, forUrl);

        return cdDto;
    }

    public List<PlayerDetailDto> selectPlayerHeroDetail(Long playerId, Map<String, Object> sessionItems) {
        String sessionBattleTag = (String) sessionItems.get("sessionBattleTag");

        log.info("{} >>>>>>>> selectPlayerHeroDetail 호출 | {} 플레이어의 영웅 상세정보 DB조회", sessionBattleTag, playerId);
        List<PlayerDetailDto> list = new ArrayList<PlayerDetailDto>();

        Long season = seasonRepository.selectSeason(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        log.debug("{} >>>>>>>> selectPlayerHeroDetail 진행중 | 현재 시즌 조회 : {}", sessionBattleTag, season);

        List<PlayerDetail> playerDetailList = playerDetailRepository.findByIdAndSeasonOrderByHeroOrderAsc(playerId,season);

        if(playerDetailList.size() == 0) {
            log.debug("{} >>>>>>>> selectPlayerHeroDetail 진행중 >>>>>>>> 현재({}) 시즌 데이터가 없어 {} 시즌 데이터를 조회합니다.", sessionBattleTag, season, season-1);
            playerDetailList = playerDetailRepository.findByIdAndSeasonOrderByHeroOrderAsc(playerId, season-1);
        }

        log.debug("{} >>>>>>>> selectPlayerHeroDetail 진행중 | {} 플레이어의 영웅 상세정보 DB조회 결과({}) ", sessionBattleTag, playerId, playerDetailList.size());
        for(PlayerDetail playerDetail : playerDetailList) {
            PlayerDetailDto playerDetailDto = new PlayerDetailDto(playerDetail.getId(), playerDetail.getSeason(), playerDetail.getHeroOrder(), playerDetail.getHeroName()
            , playerDetail.getHeroNameKR(), playerDetail.getKillPerDeath(), playerDetail.getWinRate(), playerDetail.getPlayTime()
            , playerDetail.getDeathAvg(), playerDetail.getSpentOnFireAvg(), playerDetail.getHealPerLife(), playerDetail.getBlockDamagePerLife(), playerDetail.getLastHitPerLife()
            , playerDetail.getDamageToHeroPerLife(), playerDetail.getDamageToShieldPerLife()
            , playerDetail.getIndex1(), playerDetail.getIndex2(), playerDetail.getIndex3(), playerDetail.getIndex4(), playerDetail.getIndex5()
            , playerDetail.getTitle1(), playerDetail.getTitle2(), playerDetail.getTitle3(), playerDetail.getTitle4(), playerDetail.getTitle5());

            log.debug("{} >>>>>>>> selectPlayerHeroDetail 진행중 | {} ", sessionBattleTag, playerDetailDto.toString());
            list.add(playerDetailDto);
        }
        log.info("{} >>>>>>>> selectPlayerHeroDetail 종료 | {} 플레이어의 영웅 상세정보 DB회 종료", sessionBattleTag, playerId);

        return list;
    }

    public List<TrendlindDto> selectPlayerTrendline(Long playerId, Map<String, Object> sessionItems) {
        String sessionBattleTag = (String) sessionItems.get("sessionBattleTag");

        log.info("{} >>>>>>>> selectPlayerTrendline 호출 | {} 플레이어의 추세선 DB조회", sessionBattleTag, playerId);
        List<TrendlindDto> list = new ArrayList<TrendlindDto>();
        List<Trendline> trendlineList = trendlineRepository.findTrendlinesByIdOrderByUdtDtmAsc(playerId);

        while (trendlineList.size() > 7) {
            Trendline trendline = trendlineList.get(0);
            log.debug("{} >>>>>>>> selectPlayerTrendline 진행중 | {} 플레이어의 추세선이 7개 이상이므로 가장 오래된 데이터를 삭제합니다. : {}", sessionBattleTag, playerId, trendline.toString());
            trendlineRepository.deleteByIdAndUdtDtm(trendline.getId(), trendline.getUdtDtm());
            trendlineList.remove(trendline);
        }

        log.debug("{} | selectPlayerTrendline 진행중 | {} 플레이어의 추세선 데이터({}건)", sessionBattleTag, playerId, trendlineList.size());
        for (int i = 0 ; i < trendlineList.size() ; i++ ) {
            Trendline trendline = trendlineList.get(i);

            if(i == 0) {
                TrendlindDto trendlindDto = new TrendlindDto(trendline.getId(), trendline.getUdtDtm(), trendline.getTankRatingPoint()
                        , trendline.getDealRatingPoint(), trendline.getHealRatingPoint()
                        , trendline.getTankWinGame()
                        , trendline.getTankLoseGame()
                        , trendline.getDealWinGame()
                        , trendline.getDealLoseGame()
                        , trendline.getHealWinGame()
                        , trendline.getHealLoseGame());
                log.debug("{} | selectPlayerTrendline 진행중 | {} 플레이어의 추세선 데이터({}) : {}", sessionBattleTag, playerId, i+1, trendlindDto.toString());
                list.add(trendlindDto);
            }else {
                Trendline beforeTrend = trendlineList.get(i - 1);
                TrendlindDto trendlindDto = new TrendlindDto(trendline.getId(), trendline.getUdtDtm(), trendline.getTankRatingPoint()
                        , trendline.getDealRatingPoint(), trendline.getHealRatingPoint()
                        , checkSum(trendline.getTankWinGame() , beforeTrend.getTankWinGame())
                        , checkSum(trendline.getTankLoseGame() , beforeTrend.getTankLoseGame())
                        , checkSum(trendline.getDealWinGame() , beforeTrend.getDealWinGame())
                        , checkSum(trendline.getDealLoseGame() , beforeTrend.getDealLoseGame())
                        , checkSum(trendline.getHealWinGame() , beforeTrend.getHealWinGame())
                        , checkSum(trendline.getHealLoseGame() , beforeTrend.getHealLoseGame()));
                log.debug("{} | selectPlayerTrendline 진행중 | {} 플레이어의 추세선 데이터({}) : {}", sessionBattleTag, playerId, i+1, trendlindDto.toString());
                list.add(trendlindDto);
            }
        }
        log.info("{} | selectPlayerTrendline 종료 | {} 플레이어의 추세선 DB조회 종료", sessionBattleTag, playerId);

        return list;
    }

    public Integer checkSum (Integer now, Integer before) {
        if(now - before < 0) {
            return now;
        }else {
            return now - before;
        }
    }

    public void refreshFavorite(Long clickedId, String likeOrNot, Map<String, Object> sessionItems) {
        String sessionBattleTag = (String) sessionItems.get("sessionBattleTag");
        //로그인한 유저인지 확인하여 즐겨찾기를 조회
        log.info("{} >>>>>>>> refreshFavorite 호출 | 미로그인 유저는 세션에 저장, 로그인 유저는 DB 저장", sessionBattleTag);
        SessionUser sessionUser = (SessionUser)sessionItems.get("loginUser");

        if(sessionUser == null) {
            log.debug("{} >>>>>>>> refreshFavorite | 미로그인 유저는 세션에 저장", sessionBattleTag);
            //로그인 하지 않은 유저인 경우 session을 검색
            Map<Long, FavoriteDto> favoriteList = (Map<Long, FavoriteDto>) httpSession.getAttribute("favoriteList");
            if (favoriteList != null) {
                log.debug("{} >>>>>>>> refreshFavorite | 세션에 favoriteList 있음", sessionBattleTag);
                if(favoriteList.get(clickedId) != null) {
                    if("N".equals(likeOrNot)) {
                        log.debug("{} >>>>>>>> refreshFavorite | 세션에 favoriteList에서 삭제", sessionBattleTag);
                        favoriteList.remove(clickedId);
                    }
                }else {
                    if("Y".equals(likeOrNot)) {
                        log.debug("{} >>>>>>>> refreshFavorite | 세션에 favoriteList에서 추가", sessionBattleTag);
                        favoriteList.put(clickedId, new FavoriteDto(null, clickedId, likeOrNot));
                    }
                }
            }else {
                log.debug("{} >>>>>>>> refreshFavorite | 세션에 favoriteList 없음", sessionBattleTag);
                if("Y".equals(likeOrNot)) {
                    log.debug("{} >>>>>>>> refreshFavorite | 세션에 favoriteList 저장", sessionBattleTag);
                    favoriteList = new HashMap<Long, FavoriteDto>();
                    favoriteList.put(clickedId, new FavoriteDto(null, clickedId, likeOrNot));
                    httpSession.setAttribute("favoriteList", favoriteList);
                }
            }
        }else {
            log.debug("{} >>>>>>>> refreshFavorite | 로그인한 유저로 DB에 저장", sessionBattleTag);
            favoriteRepository.save(new Favorite(sessionUser.getId(), clickedId, likeOrNot));
        }
    }
}
