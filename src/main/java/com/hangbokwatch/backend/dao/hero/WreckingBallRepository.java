package com.hangbokwatch.backend.dao.hero;

import com.hangbokwatch.backend.domain.hero.WreckingBall;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WreckingBallRepository extends JpaRepository<WreckingBall, Long> {
    //Id로 상세정보 조회
    WreckingBall findWreckingBallById(Long playerId);
}
