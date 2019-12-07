package com.hangbokwatch.backend.dao;

import com.hangbokwatch.backend.domain.Moira;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoiraRepository extends JpaRepository<Moira, Long> {
    //Id로 상세정보 조회
    Moira findMoiraById(Long playerId);
}
