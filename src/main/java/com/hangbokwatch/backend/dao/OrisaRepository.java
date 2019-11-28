package com.hangbokwatch.backend.dao;

import com.hangbokwatch.backend.domain.Orisa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrisaRepository extends JpaRepository<Orisa, Long> {
    //Id로 상세정보 조회
    Orisa findOrisaById(Long playerId);
}
