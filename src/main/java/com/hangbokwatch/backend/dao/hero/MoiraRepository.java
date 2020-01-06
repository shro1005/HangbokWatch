package com.hangbokwatch.backend.dao.hero;

import com.hangbokwatch.backend.domain.hero.Moira;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoiraRepository extends JpaRepository<Moira, Long> {
    //Id로 상세정보 조회
    Moira findMoiraById(Long playerId);
}
