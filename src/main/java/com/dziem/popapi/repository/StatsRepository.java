package com.dziem.popapi.repository;

import com.dziem.popapi.model.Stats;
import com.dziem.popapi.model.webpage.StatsWithUName;
import com.dziem.popapi.model.webpage.TimeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public interface StatsRepository extends JpaRepository<Stats, String> {
    @Query(value = """
SELECT s.user.userId, s.totalGamePlayed, s.avgScore, s.timePlayed, s.totalScoredPoints, s.numberOfWonGames, u.name
FROM Stats s
LEFT JOIN UName u ON s.user.userId = u.user.userId
""")
    List<Object[]> getStatsWithUNameRaw();

    default List<StatsWithUName> getStatsWithUName() {
        List<Object[]> result = getStatsWithUNameRaw();
        List<StatsWithUName> statsWithUNameList = new ArrayList<>();
        for (Object[] row : result) {
            StatsWithUName statsWithUName = StatsWithUName.builder()
                    .userId((String) row[0])
                    .totalGamePlayed((Long) row[1])
                    .avgScore(new BigDecimal(String.valueOf(row[2])))
                    .timePlayed(TimeConverter.convertSecondsToTime((Long) row[3]))
                    .totalScoredPoints((Long) row[4])
                    .numberOfWonGames((Integer) row[5])
                    .name((String) row[6])
                    .build();
            statsWithUNameList.add(statsWithUName);
        }
        return statsWithUNameList;
    }
}
