package com.hangbokwatch.backend.job.batch.rankingupdate;

import com.hangbokwatch.backend.domain.player.Player;
import com.hangbokwatch.backend.domain.player.PlayerForRanking;
import com.hangbokwatch.backend.dto.CompetitiveDetailDto;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@StepScope
public class RankingBaseDataUpdateBatchMainProcessor implements ItemProcessor<Player, CompetitiveDetailDto> {
    public static final String JOB_NAME = "rankingBaseDataUpdateBatch";
    private static String GET_PLAYER_PROFILE_URL = "https://playoverwatch.com/ko-kr/career/";

    @Override
    public CompetitiveDetailDto process(Player player) throws Exception {
        CompetitiveDetailDto competitiveDetailDto = new CompetitiveDetailDto();
        log.debug("{} ============================================= new player start ======================================", JOB_NAME);
        log.debug("{} >>>>>>>> rankingBaseDataUpdateProcessor | 블리자드 크롤링 시작. 크롤링할 url : {}", JOB_NAME, GET_PLAYER_PROFILE_URL+player.getPlatform()+"/"+player.getForUrl());
        Document rawData = Jsoup.connect(GET_PLAYER_PROFILE_URL+player.getPlatform()+"/"+player.getForUrl()).maxBodySize(Integer.MAX_VALUE).get();

        log.debug("{} >>>>>>>> rankingBaseDataUpdateProcessor | {} 플레이어 경쟁전 데이터 추출", JOB_NAME, player.getBattleTag());
        Elements competitiveRole = rawData.select("div.competitive-rank-role");

        Integer tankRatingPoint = 0; Integer dealRatingPoint = 0; Integer healRatingPoint = 0;
        Integer winGame = 0; Integer loseGame = 0; Integer drawGame = 0; String time = "00:00";
        Long playTime = 0l; Long spentOnFire = 0l; Integer envKill = 0;
        for (Element roleElement : competitiveRole) {
            Element roleIcon = roleElement.selectFirst("img[class=competitive-rank-role-icon]");
            if("https://static.playoverwatch.com/img/pages/career/icon-tank-8a52daaf01.png".equals(roleIcon.attr("src"))){
                log.debug("{} >>>>>>>> rankingBaseDataUpdateProcessor | {} 플레이어 탱커 탱커 및 티어 이미지 추출", JOB_NAME, player.getBattleTag());
                tankRatingPoint = Integer.parseInt(roleElement.text());

            }else if("https://static.playoverwatch.com/img/pages/career/icon-offense-6267addd52.png".equals(roleIcon.attr("src"))){
                log.debug("{} >>>>>>>> rankingBaseDataUpdateProcessor | {} 플레이어 딜러 탱커 및 티어 이미지 추출", JOB_NAME, player.getBattleTag());
                dealRatingPoint = Integer.parseInt(roleElement.text());

            }else if("https://static.playoverwatch.com/img/pages/career/icon-support-46311a4210.png".equals(roleIcon.attr("src"))){
                log.debug("{} >>>>>>>> rankingBaseDataUpdateProcessor | {} 플레이어 힐러 탱커 및 티어 이미지 추출", JOB_NAME, player.getBattleTag());
                healRatingPoint = Integer.parseInt(roleElement.text());

            }
        }

        Element competitiveDatas = rawData.selectFirst("div#competitive");
        if(competitiveDatas == null) {
            player.setIsPublic("N");
            competitiveDetailDto.setPlayer(player);
            return competitiveDetailDto;
        }

        Element heroDetails = competitiveDatas.selectFirst("div[data-category-id=0x02E00000FFFFFFFF]");
        Elements totalDatas = heroDetails.select("tr.DataTable-tableRow");

        for (Element tr : totalDatas) {
            Elements td;
            switch (tr.attr("data-stat-id")) {
                case "0x08600000000003F5":
                    td = tr.select("td");
                    winGame = Integer.parseInt(td.last().text());
                    break;
                case "0x086000000000042E":
                    td = tr.select("td");
                    loseGame = Integer.parseInt(td.last().text());
                    break;
                case "0x086000000000042F":
                    td = tr.select("td");
                    drawGame = Integer.parseInt(td.last().text());
                    break;
                case "0x0860000000000026" :  //플레이시간
                    td = tr.select("td");
                    time = td.last().text();

                    if (td.last().text().length() >= 8) {
                        playTime += Long.parseLong(time.substring(0, time.indexOf(":"))) * 60 * 60;
                    }
                    playTime += Long.parseLong(time.substring(time.lastIndexOf(":")-2, time.lastIndexOf(":"))) * 60;
                    playTime += Long.parseLong(time.substring(time.lastIndexOf(":")+1));
                    break;
                case "0x08600000000003CD" :  //폭주시
                    td = tr.select("td");
                    time = td.last().text();

                    if (td.last().text().length() >= 8) {
                        spentOnFire += Long.parseLong(time.substring(0, time.indexOf(":"))) * 60 * 60;
                    }
                    spentOnFire += Long.parseLong(time.substring(time.lastIndexOf(":")-2, time.lastIndexOf(":"))) * 60;
                    spentOnFire += Long.parseLong(time.substring(time.lastIndexOf(":")+1));
                    break;
                case "0x0860000000000363" :   //환경요소 처치
                    td = tr.select("td");
                    envKill = Integer.parseInt(td.last().text());
                    break;
                default:
                    break;
            }
        }

        PlayerForRanking playerForRanking = new PlayerForRanking(player.getId(), player.getPlayerLevel(), tankRatingPoint, dealRatingPoint, healRatingPoint,
                                                                player.getTankWinGame(), player.getTankLoseGame(), player.getDealWinGame(), player.getDealLoseGame(),
                                                                player.getHealWinGame(), player.getHealLoseGame(), winGame, loseGame, drawGame,
                                                                playTime, spentOnFire, envKill, "Y");

        competitiveDetailDto.setPlayerForRanking(playerForRanking);

        return competitiveDetailDto;
    }
}
