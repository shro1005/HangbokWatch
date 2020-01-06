package com.hangbokwatch.backend.dao.hero;

import com.hangbokwatch.backend.domain.hero.Genji;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenjiRepository extends JpaRepository<Genji, Long> {
    //Id로 상세정보 조회
    Genji findGenjiById(Long playerId);
}
