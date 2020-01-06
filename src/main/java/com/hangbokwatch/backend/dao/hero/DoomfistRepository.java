package com.hangbokwatch.backend.dao.hero;

import com.hangbokwatch.backend.domain.hero.Doomfist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoomfistRepository extends JpaRepository<Doomfist, Long> {
    //Id로 상세정보 조회
    Doomfist findDoomfistById(Long playerId);
}
