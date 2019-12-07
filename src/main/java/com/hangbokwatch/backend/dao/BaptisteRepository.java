package com.hangbokwatch.backend.dao;

import com.hangbokwatch.backend.domain.Baptiste;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaptisteRepository extends JpaRepository<Baptiste, Long> {
    //Id로 상세정보 조회
    Baptiste findBaptisteById(Long playerId);
}
