package com.hangbokwatch.backend.dao;

import com.hangbokwatch.backend.domain.Mei;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeiRepository extends JpaRepository<Mei, Long> {
    //Id로 상세정보 조회
    Mei findMeiById(Long playerId);
}
