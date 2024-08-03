package com.dziem.popapi.repository;

import com.dziem.popapi.model.YearAndPopulation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface YearAndPopulationRepository extends JpaRepository<YearAndPopulation, Long> {
}
