package com.dziem.popapi.repository;

import com.dziem.popapi.model.webpage.WeeklyUsersSummed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface WeeklyUsersSummedRepository extends JpaRepository<WeeklyUsersSummed, LocalDate> {
}
