package com.hangbokwatch.backend.dao.hero;

import com.hangbokwatch.backend.domain.hero.Mccree;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MccreeRepository extends JpaRepository<Mccree, Long> {
    //Id로 상세정보 조회
    Mccree findMccreeById(Long playerId);
}
