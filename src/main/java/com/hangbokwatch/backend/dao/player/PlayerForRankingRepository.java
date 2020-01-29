package com.hangbokwatch.backend.dao.player;

import com.hangbokwatch.backend.domain.player.PlayerForRanking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerForRankingRepository extends JpaRepository<PlayerForRanking, Long> {
    List<PlayerForRanking> findAllByIsBaseDataAndId(String isBaseData, Long id);

    // 딜러 점수 상승별 플레이어 데이터
    Page<PlayerForRanking> findPlayerForRankingsByIsBaseDataOrderByDealRatingPointDesc(String isBaseData, Pageable pageable);

    // 탱커 점수 상승별 플레이어 데이터
    Page<PlayerForRanking> findPlayerForRankingsByIsBaseDataOrderByTankRatingPointDesc(String isBaseData, Pageable pageable);

    // 힐러 점수 상승별 플레이어 데이터
    Page<PlayerForRanking> findPlayerForRankingsByIsBaseDataOrderByHealRatingPointDesc(String isBaseData, Pageable pageable);

    // 플레이 시간 상승별 플레이어 데이터
    Page<PlayerForRanking> findPlayerForRankingsByIsBaseDataOrderByPlayTimeDesc(String isBaseData, Pageable pageable);

    // 불탄시간 상승별 플레이어 데이터
    Page<PlayerForRanking> findPlayerForRankingsByIsBaseDataOrderBySpentOnFireDesc(String isBaseData, Pageable pageable);

    // 낙사왕 상승별 플레이어 데이터
    Page<PlayerForRanking> findPlayerForRankingsByIsBaseDataOrderByEnvKillDesc(String isBaseData, Pageable pageable);

}
