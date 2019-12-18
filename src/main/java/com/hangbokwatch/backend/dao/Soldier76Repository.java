package com.hangbokwatch.backend.dao;

import com.hangbokwatch.backend.domain.Soldier76;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Soldier76Repository extends JpaRepository<Soldier76, Long> {
    //Id로 상세정보 조회
    Soldier76 findSoldier76ById(Long playerId);
}
