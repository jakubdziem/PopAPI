package com.dziem.popapi.repository;

import com.dziem.popapi.model.Score;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<Score, Long> {
}
