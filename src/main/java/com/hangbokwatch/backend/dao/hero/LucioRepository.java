package com.hangbokwatch.backend.dao.hero;

import com.hangbokwatch.backend.domain.hero.Lucio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LucioRepository extends JpaRepository<Lucio, Long> {
    //Id로 상세정보 조회
    Lucio findLucioById(Long playerId);
}
