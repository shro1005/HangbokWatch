package com.hangbokwatch.backend.dao;

import com.hangbokwatch.backend.domain.Genji;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenjiRepository extends JpaRepository<Genji, Long> {
    //Id로 상세정보 조회
    Genji findGenjiById(Long playerId);
}
