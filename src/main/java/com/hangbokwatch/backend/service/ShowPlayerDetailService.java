package com.hangbokwatch.backend.service;

import com.hangbokwatch.backend.dao.*;
import com.hangbokwatch.backend.domain.*;
import com.hangbokwatch.backend.dto.CompetitiveDetailDto;
import com.hangbokwatch.backend.dto.PlayerListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.List;

@Service
public class ShowPlayerDetailService {
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    DvaRepositroy dvaRepositroy;
    @Autowired
    ReinhardtRepository reinhardtRepository;
    @Autowired
    OrisaRepository orisaRepository;
    @Autowired
    RoadHogRepository roadHogRepository;
    @Autowired
    WinstonRepository winstonRepository;
    @Autowired
    WreckingBallRepository wreckingBallRepository;
    @Autowired
    ZaryaRepository zaryaRepository;
    @Autowired
    SigmaRepository sigmaRepository;

    @Autowired
    CrawlingPlayerDataService cpd;

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
//                        PlayerListDto playerDto = cpd.crawlingPlayerProfile(playerListDto);
//                        player = new Player(playerDto.getId(), playerDto.getBattleTag(), playerDto.getPlayerName(), playerDto.getPlayerLevel(), playerDto.getForUrl(), playerDto.getIsPublic(), playerDto.getPlatform()
//                                , playerDto.getPortrait(), playerDto.getTankRatingPoint(), playerDto.getDealRatingPoint(), playerDto.getHealRatingPoint()
//                                , playerDto.getTankRatingImg(), playerDto.getDealRatingImg(), playerDto.getHealRatingImg()
//                                , playerDto.getWinGame(), playerDto.getLoseGame(), playerDto.getDrawGame(), playerDto.getMostHero1(), playerDto.getMostHero2(), playerDto.getMostHero3());
//                        playerRepository.save(player);
//                        System.out.println(forUrl + " : player 테이블 save 성공");

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
                cdDto = selectPlayerHeroDetail(player.getId(), cdDto);
                cdDto.setPlayer(player);
            }
        }
        System.out.println("showPlayerExample("+forUrl+") 종료");

        return cdDto;
    }

    public CompetitiveDetailDto selectPlayerHeroDetail(Long playerId, CompetitiveDetailDto cdDto) {
        cdDto.setDva(dvaRepositroy.findDvaById(playerId));
        cdDto.setReinhardt(reinhardtRepository.findReinhardtById(playerId));
        cdDto.setOrisa(orisaRepository.findOrisaById(playerId));
        cdDto.setRoadHog(roadHogRepository.findRoadHogById(playerId));
        cdDto.setWinston(winstonRepository.findWinstonById(playerId));
        cdDto.setZarya(zaryaRepository.findZaryaById(playerId));
        cdDto.setSigma(sigmaRepository.findSigmaById(playerId));
        cdDto.setWreckingBall(wreckingBallRepository.findWreckingBallById(playerId));
        return cdDto;
    }
}
