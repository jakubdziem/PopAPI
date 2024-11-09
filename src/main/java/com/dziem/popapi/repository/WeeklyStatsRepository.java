package com.dziem.popapi.repository;

import com.dziem.popapi.model.webpage.WeeklyStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface WeeklyStatsRepository extends JpaRepository<WeeklyStats, Integer> {
    @Query("""
        SELECT w FROM WeeklyStats w
        WHERE w.userId = 'STATS_OFF_ALL_USERS' AND w.weekStartDate = :week
    """)
    WeeklyStats getStatsOfAllUsersCombinedFromWeek(@Param("week") LocalDate week);

    @Query("""
        SELECT w FROM WeeklyStats w
        WHERE w.mode != 'COMBINED_STATS' AND w.userId LIKE 'STATS_OFF_ALL_USERS%' AND w.weekStartDate = :week
    """)
    List<WeeklyStats> getGameStatsOffAllUsersCombinedFromWeek(@Param("week") LocalDate week);
    @Query("""
        SELECT w FROM WeeklyStats w
        WHERE w.mode = 'COMBINED_STATS' AND w.weekStartDate = :week and w.userId != 'STATS_OFF_ALL_USERS'
    """)
    List<WeeklyStats> getStatsWithUNameOfAllUsersFromWeek(@Param("week") LocalDate week);
    @Query("""
        SELECT w FROM WeeklyStats w
        WHERE w.weekStartDate = :week and w.userId not like 'STATS_OFF_ALL_USERS%' and w.mode != 'COMBINED_STATS'
    """)
    List<WeeklyStats> getAllGameStatsWithUNameOfAllUsersFromWeek(@Param("week") LocalDate week);
    @Query("""
    SELECT distinct weekStartDate from WeeklyStats
""")
    List<LocalDate> getWeeks();
}
