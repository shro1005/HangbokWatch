package com.hangbokwatch.backend.dao.hero;

import com.hangbokwatch.backend.domain.hero.Dva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DvaRepositroy extends JpaRepository<Dva, Long> {
    //Id로 상세정보 조회
    Dva findDvaById(Long playerId);
}
