package com.dziem.popapi.service;

import com.dziem.popapi.mapper.ActiveUserStatsMapper;
import com.dziem.popapi.dto.webpage.ActiveUsersStatsDTO;
import com.dziem.popapi.dto.webpage.StatsWithUNameDTO;
import com.dziem.popapi.formatter.TimeConverter;
import com.dziem.popapi.model.webpage.WeeklyActiveUsersStats;
import com.dziem.popapi.repository.WeeklyActiveUsersStatsRepository;
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
    private final WeeklyActiveUsersStatsRepository weeklyActiveUsersStatsRepository;
    private final ActiveUserStatsMapper activeUserStatsMapper;
    @Override
    public List<ActiveUsersStatsDTO> getActiveUsersStatsThisWeek() {
        List<ActiveUsersStatsDTO> activeUsersStatDTOS = new ArrayList<>();
        List<StatsWithUNameDTO> statsWithUNameDTOOfAllUsersCurrent = statsPageService.getStatsWithUNameOfAllUsersCurrent();
        LocalDate latestSavedWeek = statsPageService.getWeeks().stream().max((d1, d2) -> d1.isAfter(d2) ? 1 : d1.equals(d2) ? 0 : -1).get();
        List<StatsWithUNameDTO> statsWithUNameDTOOfAllUsersFromLatestWeek = statsPageService.getStatsWithUNameOfAllUsersFromWeek(latestSavedWeek);

        for(StatsWithUNameDTO stats : statsWithUNameDTOOfAllUsersCurrent) {
            Optional<StatsWithUNameDTO> statsLatestWeekOpt = statsWithUNameDTOOfAllUsersFromLatestWeek.stream().filter(s -> s.getUserId().equals(stats.getUserId())).findFirst();
            if(statsLatestWeekOpt.isEmpty()) {
                activeUsersStatDTOS.add(ActiveUsersStatsDTO.builder()
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
                StatsWithUNameDTO statsLatestWeek = statsLatestWeekOpt.get();
                ActiveUsersStatsDTO statsWithUNameDifference = ActiveUsersStatsDTO.builder()
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
                    activeUsersStatDTOS.add(statsWithUNameDifference);
                }
            }
        }
        return activeUsersStatDTOS;
    }



    @Override
    public void saveWeeklyActiveUsersStatsSnapshot() {
        List<WeeklyActiveUsersStats> weeklyActiveUsersStats = getActiveUsersStatsThisWeek().stream().map(activeUserStatsMapper::activeUsersStatsDTOToWeeklyActiveUsersStats).toList();
        weeklyActiveUsersStatsRepository.saveAll(weeklyActiveUsersStats);
    }

    @Override
    public List<ActiveUsersStatsDTO> getActiveUsersStatsFromWeek(String week) {
        return weeklyActiveUsersStatsRepository.getAllActiveUsersStatsFromWeek(LocalDate.parse(week)).stream().map(activeUserStatsMapper::weeklyActiveUsersStatsToActiveUsersStats).toList();
    }

    @Override
    public List<LocalDate> getWeeks() {
        return weeklyActiveUsersStatsRepository.getWeeks();
    }

}
