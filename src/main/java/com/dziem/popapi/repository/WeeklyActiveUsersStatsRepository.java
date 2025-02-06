package com.dziem.popapi.repository;

import com.dziem.popapi.model.webpage.WeeklyActiveUsersStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface WeeklyActiveUsersStatsRepository extends JpaRepository<WeeklyActiveUsersStats, LocalDate> {
    @Query("""
        SELECT w FROM weekly_active_users_stats w
        WHERE w.weekStartDate = :week
    """)
    List<WeeklyActiveUsersStats> getAllActiveUsersStatsFromWeek(@Param("week") LocalDate week);
    @Query("""
    SELECT distinct weekStartDate from weekly_active_users_stats order by weekStartDate desc
""")
    List<LocalDate> getWeeks();
}
