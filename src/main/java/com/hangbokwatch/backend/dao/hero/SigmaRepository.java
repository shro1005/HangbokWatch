package com.hangbokwatch.backend.dao.hero;

import com.hangbokwatch.backend.domain.hero.Sigma;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SigmaRepository extends JpaRepository<Sigma, Long> {
    //Id로 상세정보 조회
    Sigma findSigmaById(Long playerId);
}
