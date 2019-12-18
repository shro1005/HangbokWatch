package com.hangbokwatch.backend.dao;

import com.hangbokwatch.backend.domain.Bastion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BastionRepository extends JpaRepository<Bastion, Long> {
    //Id로 상세정보 조회
    Bastion findBastionById(Long playerId);
}
