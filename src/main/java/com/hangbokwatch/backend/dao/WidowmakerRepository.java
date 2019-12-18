package com.hangbokwatch.backend.dao;

import com.hangbokwatch.backend.domain.Widowmaker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WidowmakerRepository extends JpaRepository<Widowmaker, Long> {
    //Id로 상세정보 조회
    Widowmaker findWidowmakerById(Long playerId);
}
