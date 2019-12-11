package com.hangbokwatch.backend.dao;

import com.hangbokwatch.backend.domain.PlayerDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerDetailRepository extends JpaRepository<PlayerDetail, Long> {
    List<PlayerDetail> findByIdAndSeasonOrderByHeroOrderAsc(Long id, Long season);
}
