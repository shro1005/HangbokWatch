package com.hangbokwatch.backend.dao;

import com.hangbokwatch.backend.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, String> {
    // 유저명으로 검색
    List<Player> findByPlayerName(String playerName);

    // 유저명으로 검색(대소문자 상관x)
    List<Player> findByPlayerNameIgnoreCase(String playerName);

    //배틀태그로 검색
    List<Player> findByBattleTag(String BattleTag);
    
    Player findPlayerByBattleTag(String BattleTag);

}
