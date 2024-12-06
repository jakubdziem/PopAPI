package com.dziem.popapi.repository;

import com.dziem.popapi.model.webpage.DailyUsersSummed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface DailyUsersSummedRepository extends JpaRepository<DailyUsersSummed, LocalDate> {
    //note data before 2024-11-18 was calculated by function not saved daily like the other data
    @Query("""
    SELECT d
        FROM DailyUsersSummed d
        WHERE d.day > CAST('2024-11-17' as date)
""")
    List<DailyUsersSummed> getAll();
}
