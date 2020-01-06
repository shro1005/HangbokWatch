package com.hangbokwatch.backend.service;

import com.hangbokwatch.backend.dao.*;
import com.hangbokwatch.backend.dao.player.PlayerDetailRepository;
import com.hangbokwatch.backend.dao.player.PlayerRepository;
import com.hangbokwatch.backend.dao.player.TrendlineRepository;
import com.hangbokwatch.backend.domain.player.Player;
import com.hangbokwatch.backend.domain.player.PlayerDetail;
import com.hangbokwatch.backend.domain.player.Trendline;
import com.hangbokwatch.backend.dto.CompetitiveDetailDto;
import com.hangbokwatch.backend.dto.PlayerDetailDto;
import com.hangbokwatch.backend.dto.PlayerListDto;
import com.hangbokwatch.backend.dto.TrendlindDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShowPlayerDetailService {
    @Autowired PlayerRepository playerRepository;
    @Autowired PlayerDetailRepository playerDetailRepository;
    @Autowired SeasonRepository seasonRepository;
    @Autowired TrendlineRepository trendlineRepository;

    @Autowired CrawlingPlayerDataService cpd;

    public CompetitiveDetailDto showPlayerExample(String forUrl) {
        CompetitiveDetailDto cdDto = new CompetitiveDetailDto();
        StopWatch stopWatch = new StopWatch();
        if(forUrl.indexOf("-") != -1 ) {
            forUrl = forUrl.replace("-", "#");

            /** 기존에 없는 플레이어 조회시 */
            if(playerRepository.findByBattleTag(forUrl).size() == 0) {
                stopWatch.start("API를 통한 플레이어 기본 정보 추출");
                List<PlayerListDto> playerListDtos = cpd.crawlingPlayerList(forUrl);
                stopWatch.stop();
                for (PlayerListDto playerListDto : playerListDtos) {
                    if ("Y".equals(playerListDto.getIsPublic())) {
                        stopWatch.start("경쟁정 디테일 크롤링 및 데이터 저장 까지 총 시간");
                        //
                        cdDto = cpd.crawlingPlayerDetail(playerListDto, cdDto);
                        // 시간 확인
                        stopWatch.stop();
                        System.out.println(stopWatch.prettyPrint());
                    }
                }
            /** 기존에 데이터 등록했던 플레이어 조회시*/
            }else {
                Player player = playerRepository.findPlayerByBattleTag(forUrl);
//                cdDto = selectPlayerHeroDetail(player.getId(), cdDto);
                cdDto.setPlayer(player);
            }
        }
        System.out.println("showPlayerExample("+forUrl+") 종료");

        return cdDto;
    }

    public CompetitiveDetailDto refreshPlayerDetail(String forUrl) {
        CompetitiveDetailDto cdDto = new CompetitiveDetailDto();
        StopWatch stopWatch = new StopWatch();
        if(forUrl.indexOf("-") != -1 ) {
            forUrl = forUrl.replace("-", "#");

            stopWatch.start("API를 통한 플레이어 기본 정보 추출");
            List<PlayerListDto> playerListDtos = cpd.crawlingPlayerList(forUrl);
            stopWatch.stop();
            for (PlayerListDto playerListDto : playerListDtos) {
                if ("Y".equals(playerListDto.getIsPublic())) {
                    stopWatch.start("경쟁정 디테일 크롤링 및 데이터 저장 까지 총 시간");
                    //
                    cdDto = cpd.crawlingPlayerDetail(playerListDto, cdDto);
//                        cdDto = selectPlayerHeroDetail(playerListDto.getId(), cdDto);
                    // 시간 확인
                    stopWatch.stop();
                    System.out.println(stopWatch.prettyPrint());
                }
            }
        }
        System.out.println("refreshPlayerDetail("+forUrl+") 종료");

        return cdDto;
    }

    public List<PlayerDetailDto> selectPlayerHeroDetail(Long playerId) {
        List<PlayerDetailDto> list = new ArrayList<PlayerDetailDto>();
        Long season = seasonRepository.selectSeason(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        System.out.println("========== current season : "+season + " ===========");
        List<PlayerDetail> playerDetailList = playerDetailRepository.findByIdAndSeasonOrderByHeroOrderAsc(playerId,season);
        if(playerDetailList.size() == 0) {
            System.out.println("========== " + season + "시즌 데이터가 없어 "+ (season-1) + "시즌 데이터를 조회합니다. ===========");
            playerDetailList = playerDetailRepository.findByIdAndSeasonOrderByHeroOrderAsc(playerId, season-1);
        }
        for(PlayerDetail playerDetail : playerDetailList) {
            PlayerDetailDto playerDetailDto = new PlayerDetailDto(playerDetail.getId(), playerDetail.getSeason(), playerDetail.getHeroOrder(), playerDetail.getHeroName()
            , playerDetail.getHeroNameKR(), playerDetail.getKillPerDeath(), playerDetail.getWinRate(), playerDetail.getPlayTime()
            , playerDetail.getDeathAvg(), playerDetail.getSpentOnFireAvg(), playerDetail.getHealPerLife(), playerDetail.getBlockDamagePerLife(), playerDetail.getLastHitPerLife()
            , playerDetail.getDamageToHeroPerLife(), playerDetail.getDamageToShieldPerLife()
            , playerDetail.getIndex1(), playerDetail.getIndex2(), playerDetail.getIndex3(), playerDetail.getIndex4(), playerDetail.getIndex5()
            , playerDetail.getTitle1(), playerDetail.getTitle2(), playerDetail.getTitle3(), playerDetail.getTitle4(), playerDetail.getTitle5());

//            System.out.println(playerDetailDto);
            list.add(playerDetailDto);
        }
        return list;
    }

    public List<TrendlindDto> selectPlayerTrendline(Long playerId) {
        List<TrendlindDto> list = new ArrayList<TrendlindDto>();
        List<Trendline> trendlineList = trendlineRepository.findTrendlinesByIdOrderByUdtDtmAsc(playerId);

        while (trendlineList.size() > 7) {
            Trendline trendline = trendlineList.get(0);
            trendlineRepository.deleteByIdAndUdtDtm(trendline.getId(), trendline.getUdtDtm());
            trendlineList.remove(trendline);
        }

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
                list.add(trendlindDto);
            }
        }
        return list;
    }

    public Integer checkSum (Integer now, Integer before) {
        if(now - before < 0) {
            return now;
        }else {
            return now - before;
        }
    }
}
