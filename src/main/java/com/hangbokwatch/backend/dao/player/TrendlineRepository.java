package com.hangbokwatch.backend.dao.player;

import com.hangbokwatch.backend.domain.player.Trendline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TrendlineRepository extends JpaRepository<Trendline, Long> {
    List<Trendline> findTrendlinesByIdOrderByUdtDtmAsc(Long id);

    @Transactional
    void deleteByIdAndUdtDtm(Long id, String udtDtm);
}
