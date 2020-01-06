package com.hangbokwatch.backend.dao.hero;

import com.hangbokwatch.backend.domain.hero.Symmetra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SymmetraRepository extends JpaRepository<Symmetra, Long> {
    //Id로 상세정보 조회
    Symmetra findSymmetraById(Long playerId);
}
