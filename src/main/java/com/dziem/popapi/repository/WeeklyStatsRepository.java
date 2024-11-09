package com.dziem.popapi.repository;

import com.dziem.popapi.model.webpage.WeeklyStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface WeeklyStatsRepository extends JpaRepository<WeeklyStats, LocalDate> {
}
