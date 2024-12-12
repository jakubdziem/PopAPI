package com.dziem.popapi.repository;

import com.dziem.popapi.model.webpage.WeeklyNewUsersSummed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface WeeklyNewUsersSummedRepository extends JpaRepository<WeeklyNewUsersSummed, LocalDate> {
}
