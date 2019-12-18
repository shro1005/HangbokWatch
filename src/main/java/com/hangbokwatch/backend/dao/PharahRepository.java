package com.hangbokwatch.backend.dao;

import com.hangbokwatch.backend.domain.Pharah;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharahRepository extends JpaRepository<Pharah, Long> {
    //Id로 상세정보 조회
    Pharah findPharahById(Long playerId);
}
