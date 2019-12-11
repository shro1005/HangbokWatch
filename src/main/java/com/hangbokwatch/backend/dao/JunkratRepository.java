package com.hangbokwatch.backend.dao;

import com.hangbokwatch.backend.domain.Junkrat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JunkratRepository extends JpaRepository<Junkrat, Long> {
    //Id로 상세정보 조회
    Junkrat findJunkratById(Long playerId);
}
