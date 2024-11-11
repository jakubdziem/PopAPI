package com.dziem.popapi.repository;

import com.dziem.popapi.model.webpage.DailyStatsSummed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DailyStatsSummedRepository extends JpaRepository<DailyStatsSummed, Long> {
    @Query("""
        SELECT d
        FROM DailyStatsSummed d
        WHERE d.day = :day
    """)
    List<DailyStatsSummed> findAllByDay(@Param("day") LocalDate day);
}
