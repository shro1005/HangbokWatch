package com.hangbokwatch.backend.dao;

import com.hangbokwatch.backend.domain.Zenyatta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZenyattaRepository extends JpaRepository<Zenyatta, Long> {
    //Id로 상세정보 조회
    Zenyatta findZenyattaById(Long playerId);
}
