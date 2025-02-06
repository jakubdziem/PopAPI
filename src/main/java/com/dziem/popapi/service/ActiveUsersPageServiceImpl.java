package com.dziem.popapi.service;

import com.dziem.popapi.mapper.ActiveUserStatsMapper;
import com.dziem.popapi.model.webpage.ActiveUsersStats;
import com.dziem.popapi.model.webpage.StatsWithUName;
import com.dziem.popapi.model.webpage.TimeConverter;
import com.dziem.popapi.model.webpage.WeeklyActiveUsersStats;
import com.dziem.popapi.repository.WeeklyActiveUsersStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class ActiveUsersPageServiceImpl implements ActiveUsersPageService {
    private final StatsPageService statsPageService;
    private final WeeklyActiveUsersStatsRepository weeklyActiveUsersStatsRepository;
    private final ActiveUserStatsMapper activeUserStatsMapper;
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
                        .newUser(true)
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
                        .newUser(false)
                        .build();
                if(statsWithUNameDifference.getTotalGamePlayed() > 0) {
                    activeUsersStats.add(statsWithUNameDifference);
                }
            }
        }
        return activeUsersStats;
    }



    @Scheduled(cron = "0 55 5 * * SUN", zone = "Europe/Warsaw")
    @Override
    public void saveWeeklyActiveUsersStatsSnapshot() {
        List<WeeklyActiveUsersStats> weeklyActiveUsersStats = getActiveUsersStatsThisWeek().stream().map(activeUserStatsMapper::activeUsersStatsToWeeklyActiveUsersStats).toList();
        weeklyActiveUsersStatsRepository.saveAll(weeklyActiveUsersStats);
    }

    @Override
    public List<ActiveUsersStats> getActiveUsersStatsFromWeek(String week) {
        return weeklyActiveUsersStatsRepository.getAllActiveUsersStatsFromWeek(LocalDate.parse(week)).stream().map(activeUserStatsMapper::weeklyActiveUsersStatsToActiveUsersStats).toList();
    }

    @Override
    public List<LocalDate> getWeeks() {
        return weeklyActiveUsersStatsRepository.getWeeks();
    }

}
