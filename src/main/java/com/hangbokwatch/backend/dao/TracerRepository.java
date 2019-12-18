package com.hangbokwatch.backend.dao;

import com.hangbokwatch.backend.domain.Tracer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TracerRepository extends JpaRepository<Tracer, Long> {
    //Id로 상세정보 조회
    Tracer findTracerById(Long playerId);
}
