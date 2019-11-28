package com.hangbokwatch.backend.dao;

import com.hangbokwatch.backend.domain.RoadHog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoadHogRepository extends JpaRepository<RoadHog, Long> {
    //Id로 상세정보 조회
    RoadHog findRoadHogById(Long playerId);
}
