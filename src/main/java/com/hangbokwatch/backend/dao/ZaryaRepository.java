package com.hangbokwatch.backend.dao;

import com.hangbokwatch.backend.domain.Zarya;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZaryaRepository extends JpaRepository<Zarya, Long> {
    //Id로 상세정보 조회
    Zarya findZaryaById(Long playerId);
}
