package com.hangbokwatch.backend.dao.hero;

import com.hangbokwatch.backend.domain.hero.Zarya;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZaryaRepository extends JpaRepository<Zarya, Long> {
    //Id로 상세정보 조회
    Zarya findZaryaById(Long playerId);
}
