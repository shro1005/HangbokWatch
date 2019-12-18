package com.hangbokwatch.backend.dao;

import com.hangbokwatch.backend.domain.Sombra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SombraRepository extends JpaRepository<Sombra, Long> {
    //Id로 상세정보 조회
    Sombra findSombraById(Long playerId);
}
