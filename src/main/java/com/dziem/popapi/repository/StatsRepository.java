package com.dziem.popapi.repository;

import com.dziem.popapi.model.Stats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface StatsRepository extends JpaRepository<Stats, String> {
    @Query(value = """
SELECT s.user.userId, s.totalGamePlayed, s.avgScore, s.timePlayed, s.totalScoredPoints, s.numberOfWonGames, u.name
FROM Stats s
LEFT JOIN UName u ON s.user.userId = u.user.userId
""")
    List<Object[]> getStatsWithUNameRaw();
}
