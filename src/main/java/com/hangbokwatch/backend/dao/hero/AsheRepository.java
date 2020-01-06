package com.hangbokwatch.backend.dao.hero;

import com.hangbokwatch.backend.domain.hero.Ashe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AsheRepository extends JpaRepository<Ashe, Long> {
    //Id로 상세정보 조회
    Ashe findAsheById(Long playerId);
}
