package com.dziem.popapi.repository;

import com.dziem.popapi.model.webpage.DailyUsersSummed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DailyUsersSummedRepository extends JpaRepository<DailyUsersSummed, LocalDate> {
}
