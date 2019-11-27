package com.hangbokwatch.backend.service;

import com.hangbokwatch.backend.dao.PlayerRepository;
import com.hangbokwatch.backend.domain.Player;
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
    CrawlingPlayerDataService cpd;

    public CompetitiveDetailDto showPlayerExample(String forUrl) {
        CompetitiveDetailDto cdDto = new CompetitiveDetailDto();
        StopWatch stopWatch = new StopWatch();
        if(forUrl.indexOf("-") != -1 ) {
            forUrl = forUrl.replace("-", "#");
            List<PlayerListDto> playerListDtos = cpd.crawlingPlayerList(forUrl);
            Player player = null;
            for (PlayerListDto playerListDto : playerListDtos) {
                if ("Y".equals(playerListDto.getIsPublic())) {
                    PlayerListDto playerDto = cpd.crawlingPlayerProfile(playerListDto);
                    player = new Player(playerDto.getId(), playerDto.getBattleTag(), playerDto.getPlayerName(), playerDto.getPlayerLevel(), playerDto.getForUrl(), playerDto.getIsPublic(), playerDto.getPlatform()
                            , playerDto.getPortrait(), playerDto.getTankRatingPoint(), playerDto.getDealRatingPoint(), playerDto.getHealRatingPoint(), playerDto.getWinGame()
                            , playerDto.getLoseGame(), playerDto.getDrawGame(), playerDto.getMostHero1(), playerDto.getMostHero2(), playerDto.getMostHero3());
                    playerRepository.save(player);
                    cdDto.setPlayer(player);
                    System.out.println(forUrl + " : player 테이블 save 성공");
                    //
                    stopWatch.start("경쟁정 디테일 크롤링 및 데이터 저장 까지 총 시간");
                    //
                    cdDto= cpd.crawlingPlayerDetail(playerListDto, cdDto);
                    // 시간 확인
                    stopWatch.stop();
                    System.out.println(stopWatch.prettyPrint());
                    //
                }
            }
        }
        System.out.println("savePlayerExample -> forUrl : "+forUrl);

        return cdDto;
    }
}
