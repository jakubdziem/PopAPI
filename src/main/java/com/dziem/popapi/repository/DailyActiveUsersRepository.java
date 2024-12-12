package com.dziem.popapi.repository;

import com.dziem.popapi.model.webpage.DailyActiveUsers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DailyActiveUsersRepository extends JpaRepository<DailyActiveUsers, LocalDate> {
}
