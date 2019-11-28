package com.hangbokwatch.backend.dao;

import com.hangbokwatch.backend.domain.Sigma;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SigmaRepository extends JpaRepository<Sigma, Long> {
    //Id로 상세정보 조회
    Sigma findSigmaById(Long playerId);
}
