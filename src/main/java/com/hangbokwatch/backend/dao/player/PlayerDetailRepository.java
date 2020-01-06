package com.hangbokwatch.backend.dao.player;

import com.hangbokwatch.backend.domain.player.PlayerDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PlayerDetailRepository extends JpaRepository<PlayerDetail, Long> {
    List<PlayerDetail> findByIdAndSeasonOrderByHeroOrderAsc(Long id, Long season);
    Long countByIdAndSeason(Long id, Long season);

    @Transactional
    void deletePlayerDetailsByIdAndSeason(Long id, Long season);
}
