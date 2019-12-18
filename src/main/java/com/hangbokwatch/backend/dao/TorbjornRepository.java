package com.hangbokwatch.backend.dao;

import com.hangbokwatch.backend.domain.Torbjorn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TorbjornRepository extends JpaRepository<Torbjorn, Long> {
    //Id로 상세정보 조회
    Torbjorn findTorbjornById(Long playerId);
}
