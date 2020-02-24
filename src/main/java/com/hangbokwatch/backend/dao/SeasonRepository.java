package com.hangbokwatch.backend.dao;

import com.hangbokwatch.backend.domain.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeasonRepository extends JpaRepository<Season, Long> {
    @Query(nativeQuery = true, value = "select season from season where ?1 between start_date and end_date")
    long selectSeason(String nowDate);

    List<Season> findSeasonsByEndDateGreaterThanEqualOrderBySeasonDesc(String date);
}
