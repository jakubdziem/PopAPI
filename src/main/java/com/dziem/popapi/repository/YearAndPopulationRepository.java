package com.dziem.popapi.repository;

import com.dziem.popapi.model.YearAndPopulation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface YearAndPopulationRepository extends JpaRepository<YearAndPopulation, Long> {
    List<YearAndPopulation> findByYearOfMeasurement(String yearOfMeasurement);
}
