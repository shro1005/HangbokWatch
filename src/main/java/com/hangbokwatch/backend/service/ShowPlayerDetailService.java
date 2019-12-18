package com.hangbokwatch.backend.service;

import com.hangbokwatch.backend.dao.*;
import com.hangbokwatch.backend.domain.*;
import com.hangbokwatch.backend.dto.CompetitiveDetailDto;
import com.hangbokwatch.backend.dto.PlayerDetailDto;
import com.hangbokwatch.backend.dto.PlayerListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShowPlayerDetailService {
    @Autowired PlayerRepository playerRepository;
    @Autowired PlayerDetailRepository playerDetailRepository;
    @Autowired DvaRepositroy dvaRepositroy;
    @Autowired OrisaRepository orisaRepository;
    @Autowired ReinhardtRepository reinhardtRepository;
    @Autowired ZaryaRepository zaryaRepository;
    @Autowired RoadHogRepository roadhogRepository;
    @Autowired WinstonRepository winstonRepository;
    @Autowired SigmaRepository sigmaRepository;
    @Autowired WreckingBallRepository wreckingBallRepository;
    @Autowired AnaRepository anaRepository;
    @Autowired BaptisteRepository baptisteRepository;
    @Autowired BrigitteRepository brigitteRepository;
    @Autowired LucioRepository lucioRepository;
    @Autowired MercyRepository mercyRepository;
    @Autowired MoiraRepository moiraRepository;
    @Autowired ZenyattaRepository zenyattaRepository;
    @Autowired JunkratRepository junkratRepository;
    @Autowired GenjiRepository genjiRepository;
    @Autowired DoomfistRepository doomfistRepository;
    @Autowired ReaperRepository reaperRepository;
    @Autowired MccreeRepository mccreeRepository;
    @Autowired MeiRepository meiRepository;
    @Autowired BastionRepository bastionRepository;
    @Autowired Soldier76Repository soldier76Repository;
    @Autowired SombraRepository sombraRepository;
    @Autowired SymmetraRepository symmetraRepository;
    @Autowired AsheRepository asheRepository;
    @Autowired WidowmakerRepository widowmakerRepository;
    @Autowired TorbjornRepository torbjornRepository;
    @Autowired TracerRepository tracerRepository;
    @Autowired PharahRepository pharahRepository;
    @Autowired HanzoRepository hanzoRepository;

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
//                        cdDto = selectPlayerHeroDetail(playerListDto.getId(), cdDto);
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

    public List<PlayerDetailDto> selectPlayerHeroDetail(Long playerId) {
        List<PlayerDetailDto> list = new ArrayList<PlayerDetailDto>();
        List<PlayerDetail> playerDetailList = playerDetailRepository.findByIdAndSeasonOrderByHeroOrderAsc(playerId,19l);
        for(PlayerDetail playerDetail : playerDetailList) {
            PlayerDetailDto playerDetailDto = new PlayerDetailDto(playerDetail.getId(), playerDetail.getSeason(), playerDetail.getHeroOrder(), playerDetail.getHeroName()
            , playerDetail.getHeroNameKR(), playerDetail.getKillPerDeath(), playerDetail.getWinRate(), playerDetail.getPlayTime()
            , playerDetail.getDeathAvg(), playerDetail.getSpentOnFireAvg(), playerDetail.getHealPerLife(), playerDetail.getBlockDamagePerLife(), playerDetail.getLastHitPerLife()
            , playerDetail.getDamageToHeroPerLife(), playerDetail.getDamageToShieldPerLife(), playerDetail.getIndex1(), playerDetail.getIndex2()
            , playerDetail.getIndex3(), playerDetail.getIndex4(), playerDetail.getIndex5());

//            System.out.println(playerDetailDto);
            list.add(playerDetailDto);
        }

        return list;
    }
}
