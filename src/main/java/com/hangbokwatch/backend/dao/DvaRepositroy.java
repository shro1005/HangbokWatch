package com.hangbokwatch.backend.dao;

import com.hangbokwatch.backend.domain.Dva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DvaRepositroy extends JpaRepository<Dva, Long> {
    //Id로 상세정보 조회
    Dva findDvaById(Long playerId);
}
