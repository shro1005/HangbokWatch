package com.hangbokwatch.backend.dao.hero;

import com.hangbokwatch.backend.domain.hero.Mei;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeiRepository extends JpaRepository<Mei, Long> {
    //Id로 상세정보 조회
    Mei findMeiById(Long playerId);
}
