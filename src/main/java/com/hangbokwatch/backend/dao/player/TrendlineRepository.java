package com.hangbokwatch.backend.dao.player;

import com.hangbokwatch.backend.domain.player.Trendline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrendlineRepository extends JpaRepository<Trendline, Long> {
    List<Trendline> findTrendlinesByIdOrderByUdtDtmAsc(Long id);
    void deleteByIdAndUdtDtm(Long id, String udtDtm);
}
