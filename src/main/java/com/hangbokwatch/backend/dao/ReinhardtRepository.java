package com.hangbokwatch.backend.dao;

import com.hangbokwatch.backend.domain.Reinhardt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReinhardtRepository extends JpaRepository<Reinhardt, Long> {
    //Id로 상세정보 조회
    Reinhardt findReinhardtById(Long playerId);
}
