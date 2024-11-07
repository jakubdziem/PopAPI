package com.dziem.popapi.repository;

import com.dziem.popapi.model.ModeStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ModeStatsRepository extends JpaRepository<ModeStats, Long> {
    @Query("""

            SELECT s.user.userId, s.totalGamePlayed, s.avgScore, s.timePlayed, s.totalGamePlayed, s.numberOfWonGames, u.name
FROM ModeStats as s
LEFT JOIN UName u ON s.user.userId = u.user.userId
WHERE s.mode = ?1
""")
    List<Object[]> getModeStatsWithUNameRaw(String mode);
    }

//    @Query("""
//SELECT s.totalGamePlayed, s.avgScore, s.timePlayed, s.totalGamePlayed, s.numberOfWonGames
//FROM ModeStats as s
//WHERE s.mode = ?1
//""")
//    List<ModeStats> getGameStatsOffAllUsersOfMode(String mode);
//}
