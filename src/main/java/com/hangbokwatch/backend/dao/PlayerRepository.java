package com.hangbokwatch.backend.dao;

import com.hangbokwatch.backend.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, String> {
    // 유저명으로 검색
    List<Player> findByPlayerName(String playerName);

    //배틀태그로 검색
    List<Player> findByBattleTag(String BattleTsdsag);

}
