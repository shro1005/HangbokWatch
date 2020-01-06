package com.hangbokwatch.backend.dao.hero;

import com.hangbokwatch.backend.domain.hero.Reinhardt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReinhardtRepository extends JpaRepository<Reinhardt, Long> {
    //Id로 상세정보 조회
    Reinhardt findReinhardtById(Long playerId);
}
