package com.dziem.popapi.service;

import com.dziem.popapi.model.webpage.ActiveUsersStats;
import com.dziem.popapi.model.webpage.StatsWithUName;
import com.dziem.popapi.model.webpage.TimeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class ActiveUsersPageServiceImpl implements ActiveUsersPageService {
    private final StatsPageService statsPageService;
    @Override
    public List<ActiveUsersStats> getActiveUsersStatsThisWeek() {
        List<ActiveUsersStats> activeUsersStats = new ArrayList<>();
        List<StatsWithUName> statsWithUNameOfAllUsersCurrent = statsPageService.getStatsWithUNameOfAllUsersCurrent();
        LocalDate latestSavedWeek = statsPageService.getWeeks().stream().max((d1, d2) -> d1.isAfter(d2) ? 1 : d1.equals(d2) ? 0 : -1).get();
        List<StatsWithUName> statsWithUNameOfAllUsersFromLatestWeek = statsPageService.getStatsWithUNameOfAllUsersFromWeek(latestSavedWeek);

        for(StatsWithUName stats : statsWithUNameOfAllUsersCurrent) {
            Optional<StatsWithUName> statsLatestWeekOpt = statsWithUNameOfAllUsersFromLatestWeek.stream().filter(s -> s.getUserId().equals(stats.getUserId())).findFirst();
            if(statsLatestWeekOpt.isEmpty()) {
                activeUsersStats.add(ActiveUsersStats.builder()
                        .userId(stats.getUserId())
                        .totalGamePlayed(stats.getTotalGamePlayed())
                        .avgScore(stats.getAvgScore())
                        .timePlayed(stats.getTimePlayed())
                        .totalScoredPoints(stats.getTotalScoredPoints())
                        .numberOfWonGames(stats.getNumberOfWonGames())
                        .name(stats.getName())
                        .isNew(true)
                        .build());
            } else {
                StatsWithUName statsLatestWeek = statsLatestWeekOpt.get();
                ActiveUsersStats statsWithUNameDifference = ActiveUsersStats.builder()
                        .userId(stats.getUserId())
                        .totalGamePlayed(stats.getTotalGamePlayed() - statsLatestWeek.getTotalGamePlayed())
                        .avgScore(stats.getAvgScore())
                        .timePlayed(TimeConverter.differenceOfTime(stats.getTimePlayed(), statsLatestWeek.getTimePlayed()))
                        .totalScoredPoints(stats.getTotalScoredPoints() - statsLatestWeek.getTotalScoredPoints())
                        .numberOfWonGames(stats.getNumberOfWonGames() - statsLatestWeek.getNumberOfWonGames())
                        .name(stats.getName())
                        .isNew(false)
                        .build();
                if(statsWithUNameDifference.getTotalGamePlayed() > 0) {
                    activeUsersStats.add(statsWithUNameDifference);
                }
            }
        }
        return activeUsersStats;
    }
}
