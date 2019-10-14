package com.hangbokwatch.backend.service;

import com.hangbokwatch.backend.dao.PlayerRepository;
import com.hangbokwatch.backend.domain.Player;
import com.hangbokwatch.backend.dto.PlayerListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.List;

@Service
public class SavePlayerDetailService {
    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    CrawlingPlayerDataService cpd;

    public void savePlayerExample(String forUrl) {
        StopWatch stopWatch = new StopWatch();
        String forUrl2 = forUrl;
        if(forUrl.indexOf("-") != -1 ) {
            forUrl2 = forUrl.substring(0, forUrl.indexOf("-")) + "%23" + forUrl.substring(forUrl.indexOf("-") + 1, forUrl.length());
            List<PlayerListDto> playerListDtos = cpd.crawlingPlayerList(forUrl2);
            for (PlayerListDto playerListDto : playerListDtos) {
                if ("Y".equals(playerListDto.getIsPublic())){
                    PlayerListDto playerDto = cpd.crawlingPlayerProfile(playerListDto);
                    Player player = new Player(playerDto.getId(), playerDto.getBattleTag(), playerDto.getPlayerName(), playerDto.getPlayerLevel(), playerDto.getIsPublic(), playerDto.getPlatform()
                            , playerDto.getPortrait(), playerDto.getTankRatingPoint(), playerDto.getDealRatingPoint(), playerDto.getHealRatingPoint(), playerDto.getWinGame()
                            , playerDto.getLoseGame(), playerDto.getDrawGame(), playerDto.getMostHero1(), playerDto.getMostHero2(), playerDto.getMostHero3());
                    playerRepository.save(player);
                    System.out.println(forUrl + " : player 테이블 save 성공");
                    //
                    stopWatch.start("경쟁정 디테일 크롤링 및 데이터 저장 까지 총 시간");
                    //
                    cpd.crawlingPlayerDetail(playerListDto);
                    // 시간 확인
                    stopWatch.stop();
                    System.out.println(stopWatch.prettyPrint());
                    //
                }
            }
        }
        System.out.println("savePlayerExample -> forUrl : "+forUrl2);

        PlayerListDto playerListDto = new PlayerListDto();
    }
}
