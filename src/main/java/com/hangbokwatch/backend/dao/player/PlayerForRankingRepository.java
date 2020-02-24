package com.hangbokwatch.backend.dao.player;

import com.hangbokwatch.backend.domain.player.PlayerForRanking;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface PlayerForRankingRepository extends JpaRepository<PlayerForRanking, Long> {
    PlayerForRanking findPlayerForRankingByIsBaseDataAndId(String isBaseData, Long id);

    List<PlayerForRanking> findAllByIsBaseDataAndId(String isBaseData, Long id);

    int countByIsBaseDataAndId(String isBaseData, Long id);

    @Query(value = "SELECT e.udt_dtm FROM forranking e WHERE e.is_base = ?1 ORDER BY e.udt_dtm DESC offset ?2 limit ?3", nativeQuery = true)
    LocalDateTime selectMaxUdtDtmWhereBase(String isBaseData, int offset, int limit);

    // @Query(value="SELECT * FROM forranking e WHERE e.is_base LIKE ?1 ORDER BY e.deal_rating_point DESC offset ?2 limit ?3", nativeQuery = true)
    // 탱커 점수 상승별 플레이어 데이터
    @Query(value="SELECT * FROM forranking e WHERE e.is_base LIKE ?1 AND e.udt_dtm >= ?4 ORDER BY e.tank_rating_point DESC offset ?2 limit ?3", nativeQuery = true)
    List<PlayerForRanking> selectAllFromPlyaerForRankingOrderByTankRatingPointDesc(String isBaseData, int offset, int limit, LocalDateTime udtDtm);
    // 딜러 점수 상승별 플레이어 데이터
    @Query(value="SELECT * FROM forranking e WHERE e.is_base LIKE ?1 AND e.udt_dtm >= ?4 ORDER BY e.deal_rating_point DESC offset ?2 limit ?3", nativeQuery = true)
    List<PlayerForRanking> selectAllFromPlyaerForRankingOrderByDealRatingPointDesc(String isBaseData, int offset, int limit, LocalDateTime udtDtm);
    // 힐러 점수 상승별 플레이어 데이터
    @Query(value="SELECT * FROM forranking e WHERE e.is_base LIKE ?1 AND e.udt_dtm >= ?4 ORDER BY e.heal_rating_point DESC offset ?2 limit ?3", nativeQuery = true)
    List<PlayerForRanking> selectAllFromPlyaerForRankingOrderByHealRatingPointDesc(String isBaseData, int offset, int limit, LocalDateTime udtDtm);
    // 플레이타임 상승별 플레이어 데이터
    @Query(value="SELECT * FROM forranking e WHERE e.is_base LIKE ?1 AND e.udt_dtm >= ?4 ORDER BY e.play_time DESC offset ?2 limit ?3", nativeQuery = true)
    List<PlayerForRanking> selectAllFromPlyaerForRankingOrderByPlayTimeDesc(String isBaseData, int offset, int limit, LocalDateTime udtDtm);
    // 폭주시간 상승별 플레이어 데이터
    @Query(value="SELECT * FROM forranking e WHERE e.is_base LIKE ?1 AND e.udt_dtm >= ?4 ORDER BY e.spent_on_fire DESC offset ?2 limit ?3", nativeQuery = true)
    List<PlayerForRanking> selectAllFromPlyaerForRankingOrderBySpentOnFireDesc(String isBaseData, int offset, int limit, LocalDateTime udtDtm);
    // 황경요소 처치 점수 상승별 플레이어 데이터
    @Query(value="SELECT * FROM forranking e WHERE e.is_base LIKE ?1 AND e.udt_dtm >= ?4 ORDER BY e.env_kill DESC offset ?2 limit ?3", nativeQuery = true)
    List<PlayerForRanking> selectAllFromPlyaerForRankingOrderByEnvKillDesc(String isBaseData, int offset, int limit, LocalDateTime udtDtm);

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
