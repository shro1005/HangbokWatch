package com.hangbokwatch.backend.dao.hero;

import com.hangbokwatch.backend.domain.hero.Soldier76;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Soldier76Repository extends JpaRepository<Soldier76, Long> {
    //Id로 상세정보 조회
    Soldier76 findSoldier76ById(Long playerId);
}
