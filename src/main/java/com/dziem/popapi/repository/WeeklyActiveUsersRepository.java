package com.dziem.popapi.repository;

import com.dziem.popapi.model.webpage.WeeklyActiveUsers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface WeeklyActiveUsersRepository extends JpaRepository<WeeklyActiveUsers, LocalDate> {
}
