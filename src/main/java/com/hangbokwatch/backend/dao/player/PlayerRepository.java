package com.hangbokwatch.backend.dao.player;

import com.hangbokwatch.backend.domain.player.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, String> {
    // 유저명으로 검색
    List<Player> findByPlayerName(String playerName);

    // 유저명으로 검색(대소문자 상관x)
    List<Player> findByPlayerNameIgnoreCase(String playerName);

    //배틀태그로 검색
    List<Player> findByBattleTag(String BattleTag);
    
    Player findPlayerByBattleTag(String BattleTag);

    // id로 검색
    Player findPlayerById(Long id);

    // 랭커 조회
    @Query(value="SELECT * FROM player p WHERE p.is_public = ?1 ORDER BY p.tank_rating_point DESC offset ?2 limit ?3", nativeQuery = true)
    List<Player> selectAllFromTankRatingDesc(String isPublic, int offset, int limit);
    @Query(value="SELECT * FROM player p WHERE p.is_public = ?1 ORDER BY p.deal_rating_point DESC offset ?2 limit ?3", nativeQuery = true)
    List<Player> selectAllFromDealRatingDesc(String isPublic, int offset, int limit);
    @Query(value="SELECT * FROM player p WHERE p.is_public = ?1 ORDER BY p.heal_rating_point DESC offset ?2 limit ?3", nativeQuery = true)
    List<Player> selectAllFromHealRatingDesc(String isPublic, int offset, int limit);
    @Query(value="SELECT * FROM player p WHERE p.is_public = ?1 ORDER BY p.total_avg_rating_point DESC offset ?2 limit ?3", nativeQuery = true)
    List<Player> selectAllFromTotalRatingDesc(String isPublic, int offset, int limit);

    Page<Player> findAllByIsPublicOrderByTankRatingPointDesc(String isPublic, Pageable pageable);
    Page<Player> findAllByIsPublicOrderByDealRatingPointDesc(String isPublic, Pageable pageable);
    Page<Player> findAllByIsPublicOrderByHealRatingPointDesc(String isPublic, Pageable pageable);
    Page<Player> findAllByIsPublicOrderByTotalAvgRatingPointDesc(String isPublic, Pageable pageable);


}
