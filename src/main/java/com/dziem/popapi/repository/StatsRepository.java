package com.dziem.popapi.repository;

import com.dziem.popapi.model.Stats;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StatsRepository extends JpaRepository<Stats, String> {
}
