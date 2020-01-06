package com.hangbokwatch.backend.dao.hero;

import com.hangbokwatch.backend.domain.hero.Ana;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnaRepository extends JpaRepository<Ana, Long> {
    //Id로 상세정보 조회
    Ana findAnaById(Long playerId);
}
