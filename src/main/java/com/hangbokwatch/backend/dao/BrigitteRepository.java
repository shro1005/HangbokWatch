package com.hangbokwatch.backend.dao;

import com.hangbokwatch.backend.domain.Brigitte;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrigitteRepository extends JpaRepository<Brigitte, Long> {
    //Id로 상세정보 조회
    Brigitte findBrigitteById(Long playerId);
}
