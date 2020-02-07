package com.hangbokwatch.backend.service;

import com.hangbokwatch.backend.dao.player.PlayerForRankingRepository;
import com.hangbokwatch.backend.domain.player.Player;
import com.hangbokwatch.backend.domain.player.PlayerForRanking;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetRankingDataService {
    @Autowired
    PlayerForRankingRepository playerForRankingRepository;

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
}
