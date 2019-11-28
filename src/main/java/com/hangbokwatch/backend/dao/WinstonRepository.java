package com.hangbokwatch.backend.dao;

import com.hangbokwatch.backend.domain.Winston;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WinstonRepository extends JpaRepository<Winston, Long> {
    //Id로 상세정보 조회
    Winston findWinstonById(Long playerId);
}
