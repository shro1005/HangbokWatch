package com.hangbokwatch.backend.service;

import com.hangbokwatch.backend.dao.player.PlayerForRankingRepository;
import com.hangbokwatch.backend.dao.player.PlayerRepository;
import com.hangbokwatch.backend.domain.player.Player;
import com.hangbokwatch.backend.domain.player.PlayerForRanking;
import com.hangbokwatch.backend.dto.PlayerListDto;
import com.hangbokwatch.backend.dto.PlayerRankingDto;
import com.hangbokwatch.backend.util.OffsetBasedPageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetRankingDataService {
    @Autowired
    PlayerForRankingRepository playerForRankingRepository;
    @Autowired
    PlayerRepository playerRepository;

    private final HttpSession httpSession;

    public PlayerForRanking updateRankingData(String sessionBattleTag, Player player) {
        Long playerId = player.getId();
        log.info("{} >>>>>>>> updateRankingData 호출 | 베이스 기록을 조회할 플레이어 id : {}", sessionBattleTag, playerId);

        PlayerForRanking playerForRanking = playerForRankingRepository.findPlayerForRankingByIsBaseDataAndId("Y", playerId);
        log.debug("{} >>>>>>>> updateRankingData 진행 | 베이스 기록을 조회완료 : {}", sessionBattleTag, playerForRanking.toString());
        // (현재 - 기준) 값으로 금주의 랭킹을 보여주기 위함
        Integer tankRatingPoint = 0; Integer dealRatingPoint =0; Integer healRatingPoint =0; int cnt = 0;
        if(playerForRanking.getTankRatingPoint() != 0) {
            tankRatingPoint = player.getTankRatingPoint() - playerForRanking.getTankRatingPoint();
        } else {
            playerForRanking.updateTankRankingPoint(player.getTankRatingPoint());
            cnt++;
        }
        if(playerForRanking.getDealRatingPoint() != 0) {
            dealRatingPoint = player.getDealRatingPoint() - playerForRanking.getDealRatingPoint();
        } else {
            playerForRanking.updateDealRankingPoint(player.getDealRatingPoint());
            cnt++;
        }
        if(playerForRanking.getHealRatingPoint() != 0) {
            healRatingPoint = player.getHealRatingPoint() - playerForRanking.getHealRatingPoint();
        } else {
            playerForRanking.updateHealRankingPoint(player.getHealRatingPoint());
            cnt++;
        }

        if(cnt > 0) { playerForRankingRepository.save(playerForRanking); }

        Long playTime = 0l; Long spentOnFire = 0l; Integer envKill = 0;
        Integer winGame = 0; Integer loseGame = 0; Integer drawGame = 0;
        if (player.getPlayTime() != null && playerForRanking.getPlayTime() != null) {
            playTime = player.getPlayTime() - playerForRanking.getPlayTime();
        }
        if (player.getSpentOnFire() != null && playerForRanking.getSpentOnFire() != null) {
            spentOnFire = player.getSpentOnFire() - playerForRanking.getSpentOnFire();
        }
        if (player.getEnvKill() != null && playerForRanking.getEnvKill() != null) {
            envKill = player.getEnvKill() - playerForRanking.getEnvKill();
        }
        if (player.getWinGame() != null && playerForRanking.getWinGame() != null) {
            winGame = player.getWinGame() - playerForRanking.getWinGame();
        }
        if (player.getLoseGame() != null && playerForRanking.getLoseGame() != null) {
            loseGame = player.getLoseGame() - playerForRanking.getLoseGame();
        }
        if (player.getDrawGame() != null && playerForRanking.getDrawGame() != null) {
            drawGame = player.getDrawGame() - playerForRanking.getDrawGame();
        }

        PlayerForRanking showingData = new PlayerForRanking(playerId, playerForRanking.getPlayerLevel(), tankRatingPoint, dealRatingPoint, healRatingPoint,
                playerForRanking.getTankWinGame(), playerForRanking.getTankLoseGame(), playerForRanking.getDealWinGame(), playerForRanking.getDealLoseGame(),
                playerForRanking.getHealWinGame(), playerForRanking.getHealLoseGame(), winGame, loseGame, drawGame,  playTime, spentOnFire, envKill, "N");
        log.info("{} >>>>>>>> updateRankingData 종료 | (현재기록-베이스기록) 측정 완료 및 저장 : {}", sessionBattleTag, showingData.toString());

        if(!"allPlayerRefreshBatch".equals(sessionBattleTag)) {
            playerForRankingRepository.save(showingData);
        }
        return showingData;
    }

    public LocalDateTime getLastUdtDtmBase() {
        return playerForRankingRepository.selectMaxUdtDtmWhereBase("Y",0,1);
    }

    public List<PlayerRankingDto> getTankRatingRankingData(Map<String, Object> sessionItems) {
        String sessionBattleTag = (String) sessionItems.get("sessionBattleTag");
        List<PlayerRankingDto> playerRankingDtoList = new ArrayList<>();

        log.info("{} >>>>>>>> getTankRatingRankingData 호출 | 탱커 점수 상승폭 랭킹 데이터 추출 시작 ", sessionBattleTag);
//        Pageable page = new OffsetBasedPageRequest(3, 3);
//        Pageable page = PageRequest.of(1,3);
//        List<PlayerForRanking> rankingDataList = playerForRankingRepository.findPlayerForRankingsByIsBaseDataOrderByTankRatingPointDesc("N", page).getContent();
        List<PlayerForRanking> rankingDataList = playerForRankingRepository.selectAllFromPlyaerForRankingOrderByTankRatingPointDesc("N",0,3, getLastUdtDtmBase());
        playerRankingDtoList = parseDomainToDTO(rankingDataList, "TR");
        log.info("{} >>>>>>>> getTankRatingRankingData 종료 | 탱커 점수 상승폭 랭킹 데이터 추출 완료 ", sessionBattleTag);

        return playerRankingDtoList;
    }

    public List<PlayerRankingDto> getDealRatingRankingData(Map<String, Object> sessionItems) {
        String sessionBattleTag = (String) sessionItems.get("sessionBattleTag");
        List<PlayerRankingDto> playerRankingDtoList = new ArrayList<>();

        log.info("{} >>>>>>>> getDealRatingRankingData 호출 | 딜러 점수 상승폭 랭킹 데이터 추출 시작 ", sessionBattleTag);
//        Pageable page = PageRequest.of(1,3);
//        List<PlayerForRanking> rankingDataList = playerForRankingRepository.findPlayerForRankingsByIsBaseDataOrderByDealRatingPointDesc("N", page).getContent();
        List<PlayerForRanking> rankingDataList = playerForRankingRepository.selectAllFromPlyaerForRankingOrderByDealRatingPointDesc("N", 0,3, getLastUdtDtmBase());
        playerRankingDtoList = parseDomainToDTO(rankingDataList, "DR");
        log.info("{} >>>>>>>> getDealRatingRankingData 종료 | 딜러 점수 상승폭 랭킹 데이터 추출 완료 ", sessionBattleTag);

        return playerRankingDtoList;
    }

    public List<PlayerRankingDto> getHealRatingRankingData(Map<String, Object> sessionItems) {
        String sessionBattleTag = (String) sessionItems.get("sessionBattleTag");
        List<PlayerRankingDto> playerRankingDtoList = new ArrayList<>();

        log.info("{} >>>>>>>> getHealRatingRankingData 호출 | 힐러 점수 상승폭 랭킹 데이터 추출 시작 ", sessionBattleTag);
//        Pageable page = PageRequest.of(1,3);
//        List<PlayerForRanking> rankingDataList = playerForRankingRepository.findPlayerForRankingsByIsBaseDataOrderByHealRatingPointDesc("N", page).getContent();
        List<PlayerForRanking> rankingDataList = playerForRankingRepository.selectAllFromPlyaerForRankingOrderByHealRatingPointDesc("N", 0,3, getLastUdtDtmBase());
        playerRankingDtoList = parseDomainToDTO(rankingDataList, "HR");
        log.info("{} >>>>>>>> getHealRatingRankingData 종료 | 힐러 점수 상승폭 랭킹 데이터 추출 완료 ", sessionBattleTag);

        return playerRankingDtoList;
    }

    public List<PlayerRankingDto> getPlayTimeRankingData(Map<String, Object> sessionItems) {
        String sessionBattleTag = (String) sessionItems.get("sessionBattleTag");
        List<PlayerRankingDto> playerRankingDtoList = new ArrayList<>();

        log.info("{} >>>>>>>> getPlayTimeRankingData 호출 | 플레이시간 상승폭 랭킹 데이터 추출 시작 ", sessionBattleTag);
//        Pageable page = PageRequest.of(1,3);
//        List<PlayerForRanking> rankingDataList = playerForRankingRepository.findPlayerForRankingsByIsBaseDataOrderByPlayTimeDesc("N", page).getContent();
        List<PlayerForRanking> rankingDataList = playerForRankingRepository.selectAllFromPlyaerForRankingOrderByPlayTimeDesc("N", 0,3, getLastUdtDtmBase());
        playerRankingDtoList = parseDomainToDTO(rankingDataList, "PT");
        log.info("{} >>>>>>>> getPlayTimeRankingData 종료 | 플레이시간 상승폭 랭킹 데이터 추출 완료 ", sessionBattleTag);

        return playerRankingDtoList;
    }

    public List<PlayerRankingDto> getSpentOnFireRankingData(Map<String, Object> sessionItems) {
        String sessionBattleTag = (String) sessionItems.get("sessionBattleTag");
        List<PlayerRankingDto> playerRankingDtoList = new ArrayList<>();

        log.info("{} >>>>>>>> getSpentOnFireRankingData 호출 | 불탄시간 상승폭 랭킹 데이터 추출 시작 ", sessionBattleTag);
//        Pageable page = PageRequest.of(1,3);
//        List<PlayerForRanking> rankingDataList = playerForRankingRepository.findPlayerForRankingsByIsBaseDataOrderBySpentOnFireDesc("N", page).getContent();
        List<PlayerForRanking> rankingDataList = playerForRankingRepository.selectAllFromPlyaerForRankingOrderBySpentOnFireDesc("N", 0,3, getLastUdtDtmBase());
        playerRankingDtoList = parseDomainToDTO(rankingDataList, "SF");
        log.info("{} >>>>>>>> getSpentOnFireRankingData 종료 | 불탄시간 상승폭 랭킹 데이터 추출 완료 ", sessionBattleTag);

        return playerRankingDtoList;
    }

    public List<PlayerRankingDto> getEnvKillRankingData(Map<String, Object> sessionItems) {
        String sessionBattleTag = (String) sessionItems.get("sessionBattleTag");
        List<PlayerRankingDto> playerRankingDtoList = new ArrayList<>();

        log.info("{} >>>>>>>> getEnvKillRankingData 호출 | 환경요소처치 랭킹 데이터 추출 시작 ", sessionBattleTag);
//        Pageable page = PageRequest.of(1,3);
//        List<PlayerForRanking> rankingDataList = playerForRankingRepository.findPlayerForRankingsByIsBaseDataOrderByEnvKillDesc("N", page).getContent();
        List<PlayerForRanking> rankingDataList = playerForRankingRepository.selectAllFromPlyaerForRankingOrderByEnvKillDesc("N", 0,3, getLastUdtDtmBase());
        playerRankingDtoList = parseDomainToDTO(rankingDataList, "EK");
        log.info("{} >>>>>>>> getEnvKillRankingData 종료 | 환경요소처치 랭킹 데이터 추출 완료 ", sessionBattleTag);

        return playerRankingDtoList;
    }

    public List<PlayerListDto> getTankRankergData(Map<String, Object> sessionItems, int offset, int limit) {
        String sessionBattleTag = (String) sessionItems.get("sessionBattleTag");
        List<PlayerListDto> playerListDtoList = new ArrayList<>();

        log.info("{} >>>>>>>> getTankRankergData 호출 | 탱커 랭커 데이터 추출 시작 ", sessionBattleTag);
//        Pageable page = PageRequest.of(1,25);
        List<Player> rankerDataList = playerRepository.selectAllFromTankRatingDesc("Y", offset, limit);
        playerListDtoList = parseDomainToDTO2(rankerDataList);
        log.info("{} >>>>>>>> getTankRankergData 종료 | 탱커 랭커 데이터 추출 완료 ", sessionBattleTag);

        return playerListDtoList;
    }
    public List<PlayerListDto> getDealRankerData(Map<String, Object> sessionItems, int offset, int limit) {
        String sessionBattleTag = (String) sessionItems.get("sessionBattleTag");
        List<PlayerListDto> playerListDtoList = new ArrayList<>();

        log.info("{} >>>>>>>> getDealRankerData 호출 | 딜러 랭커 데이터 추출 시작 ", sessionBattleTag);
//        Pageable page = PageRequest.of(1,25);
        List<Player> rankerDataList = playerRepository.selectAllFromDealRatingDesc("Y", offset, limit);
        playerListDtoList = parseDomainToDTO2(rankerDataList);
        log.info("{} >>>>>>>> getDealRankerData 종료 | 딜러 랭커 데이터 추출 완료 ", sessionBattleTag);

        return playerListDtoList;
    }

    public List<PlayerListDto> getHealRankerData(Map<String, Object> sessionItems, int offset, int limit) {
        String sessionBattleTag = (String) sessionItems.get("sessionBattleTag");
        List<PlayerListDto> playerListDtoList = new ArrayList<>();

        log.info("{} >>>>>>>> getHealRankerData 호출 | 힐러 랭커 데이터 추출 시작 ", sessionBattleTag);
//        Pageable page = PageRequest.of(1,25);
        List<Player> rankerDataList = playerRepository.selectAllFromHealRatingDesc("Y", offset, limit);
        playerListDtoList = parseDomainToDTO2(rankerDataList);
        log.info("{} >>>>>>>> getHealRankerData 종료 | 힐러 랭커 데이터 추출 완료 ", sessionBattleTag);

        return playerListDtoList;
    }

    public List<PlayerListDto> getTotalAvgRankerData(Map<String, Object> sessionItems, int offset, int limit) {
        String sessionBattleTag = (String) sessionItems.get("sessionBattleTag");
        List<PlayerListDto> playerListDtoList = new ArrayList<>();

        log.info("{} >>>>>>>> getHealRankerData 호출 | 힐러 랭커 데이터 추출 시작 ", sessionBattleTag);
//        Pageable page = PageRequest.of(1,25);
        List<Player> rankerDataList = playerRepository.selectAllFromTotalRatingDesc("Y", offset, limit);
        playerListDtoList = parseDomainToDTO2(rankerDataList);
        log.info("{} >>>>>>>> getHealRankerData 종료 | 힐러 랭커 데이터 추출 완료 ", sessionBattleTag);

        return playerListDtoList;
    }

    public List<PlayerListDto> getRankerData(Map<String, Object> sessionItems, String target, int offset, int limit) {
        String sessionBattleTag = (String) sessionItems.get("sessionBattleTag");
        List<PlayerListDto> playerListDtoList = new ArrayList<>();

        log.info("{} >>>>>>>> getHealRankerData 호출 | {} 랭커 데이터 추출 시작 ", sessionBattleTag, target);
        List<Player> rankerDataList = new ArrayList<>();
        switch (target) {
            case "total" :
                rankerDataList = playerRepository.selectAllFromTotalRatingDesc("Y", offset, limit);
                break;
            case "tank" :
                rankerDataList = playerRepository.selectAllFromTankRatingDesc("Y", offset, limit);
                break;
            case "deal" :
                rankerDataList = playerRepository.selectAllFromDealRatingDesc("Y", offset, limit);
                break;
            case "heal" :
                rankerDataList = playerRepository.selectAllFromHealRatingDesc("Y", offset, limit);
                break;
        }
        playerListDtoList = parseDomainToDTO2(rankerDataList);
        log.info("{} >>>>>>>> getHealRankerData 종료 | 랭커 데이터 추출 완료 ", sessionBattleTag);

        return playerListDtoList;
    }

    public List<PlayerRankingDto> parseDomainToDTO(List<PlayerForRanking> rankingDataList, String index) {
        List<PlayerRankingDto> playerRankingDtoList = new ArrayList<>();
        for (PlayerForRanking data : rankingDataList) {
//            System.out.println(data);
//            System.out.println(data.getId());
            Player player = playerRepository.findPlayerById(data.getId());
            String score = ""; String className = "";
            switch (index) {
                case "TR" :
                    score = data.getTankRatingPoint() + "점 상승";
                    className = "tank-rating-ranking";
                    break;
                case "DR" :
                    score = data.getDealRatingPoint() + "점 상승";
                    className = "deal-rating-ranking";
                    break;
                case "HR" :
                    score = data.getHealRatingPoint() + "점 상승";
                    className = "heal-rating-ranking";
                    break;
                case "PT" :
                    score = changeTimeToString(data.getPlayTime()) + " 플레이";
                    className = "playtime-ranking";
                    break;
                case "SF" :
                    score = changeTimeToString(data.getSpentOnFire()) + " 폭주";
                    className = "spendonfire-ranking";
                    break;
                case "EK" :
                    score = data.getEnvKill() + "회 ";
                    className = "envkill-ranking";
                    break;
                default:
                    break;
            }

            PlayerRankingDto playerRankingDto = new PlayerRankingDto(data.getId(), player.getBattleTag(),
                    player.getPlayerName(), player.getPortrait(), score, className);

            playerRankingDtoList.add(playerRankingDto);
        }
        return playerRankingDtoList;
    }

    public List<PlayerListDto> parseDomainToDTO2(List<Player> playerList) {
        List<PlayerListDto> playerDtoList = new ArrayList<>();
        for (Player player : playerList) {
            Double winRate = (new Double(player.getWinGame())/new Double(player.getWinGame()+player.getLoseGame())*100);
            Integer winRateInt = (int) (double) winRate;

            PlayerListDto playerListDto = new PlayerListDto(player.getId(), player.getBattleTag(), player.getPlayerName(), player.getForUrl(), player.getPlayerLevel()
                    , player.getIsPublic(), player.getPlatform(), player.getPortrait(), player.getTankRatingPoint(), player.getDealRatingPoint(), player.getHealRatingPoint()
                    , player.getTotalAvgRatingPoint(), player.getTankRatingImg(), player.getDealRatingImg(), player.getHealRatingImg()
                    , player.getWinGame(), player.getLoseGame(), player.getDrawGame(), Integer.toString(winRateInt)
                    , player.getMostHero1(), player.getMostHero2(), player.getMostHero3(), player.getUdtDtm().format(DateTimeFormatter.ISO_DATE));

            playerDtoList.add(playerListDto);
        }
        return playerDtoList;
    }

    public String changeTimeToString(long time) {
        String result= "";
        String hour = "0"; String minute = "00"; String second = "00";
        if(time >= 3600l) {
            hour = Long.toString(time/3600);
            time = time%3600;
        }
        if(time >= 60l) {
            minute = Long.toString(time/60);
            time = time%60;
        }
        if(time > 0) {
            second = Long.toString(time);

        }
        if(!"0".equals(hour)) { result += hour + ":";}
        result += minute + ":" + second;
        return result;
    }
}
