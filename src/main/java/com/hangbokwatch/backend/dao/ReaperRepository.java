package com.hangbokwatch.backend.dao;

import com.hangbokwatch.backend.domain.Reaper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReaperRepository extends JpaRepository<Reaper, Long> {
    //Id로 상세정보 조회
    Reaper findReaperById(Long playerId);
}
