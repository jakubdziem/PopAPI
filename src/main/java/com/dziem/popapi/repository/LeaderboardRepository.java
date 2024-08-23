package com.dziem.popapi.repository;

import com.dziem.popapi.model.Leaderboard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaderboardRepository extends JpaRepository<Leaderboard, Long> {

}
