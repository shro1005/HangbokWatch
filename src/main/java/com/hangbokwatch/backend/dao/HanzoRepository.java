package com.hangbokwatch.backend.dao;

import com.hangbokwatch.backend.domain.Hanzo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HanzoRepository extends JpaRepository<Hanzo, Long> {
    //Id로 상세정보 조회
    Hanzo findHanzoById(Long playerId);
}
