package com.dziem.popapi.service;

import com.dziem.popapi.model.Mode;
import com.dziem.popapi.model.ModeStats;
import com.dziem.popapi.model.Stats;
import com.dziem.popapi.model.UName;
import com.dziem.popapi.model.webpage.StatsWithUName;
import com.dziem.popapi.model.webpage.TimeConverter;
import com.dziem.popapi.repository.ModeStatsRepository;
import com.dziem.popapi.repository.StatsRepository;
import com.dziem.popapi.repository.UNameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatsPageServiceImpl implements StatsPageService {
    private final StatsRepository statsRepository;
    private final UNameRepository uNameRepository;
    private final ModeStatsRepository modeStatsRepository;
    @Override
    public List<StatsWithUName> getStatsWithUNameOfAllUsers() {
        return statsRepository.getStatsWithUName();
    }

    @Override
    public List<StatsWithUName> getGameStatsWithUNameOfAllUsers(Mode mode) {
        return null;
    }

    @Override
    public Map<Mode, StatsWithUName> getAllGameStatsWithUNameOfAllUsers() {
        List<ModeStats> modeStatsList = modeStatsRepository.findAll();
        List<UName> nicknames = uNameRepository.findAll();
        for(Mode mode : Mode.values()) {
            List<ModeStats> modeStatsListSpecificMode = modeStatsList.stream().filter(modeStats -> modeStats.getMode().equals(mode.toString())).toList();
            for(ModeStats modeStat : modeStatsListSpecificMode) {
                //to do
            }
        }
        return null;
    }

    @Override
    public StatsWithUName getStatsOfAllUsersCombined() {
        List<Stats> stats = statsRepository.findAll();
        Long totalGamePlayed = 0L;
        BigDecimal avgScoreSummed = BigDecimal.ZERO;
        Long timePlayedSeconds = 0L;
        Long totalScoredPoints = 0L;
        Integer numberOfWonGames = 0;
        for(Stats stat : stats) {
            totalGamePlayed += stat.getTotalGamePlayed();
            avgScoreSummed = avgScoreSummed.add(stat.getAvgScore());
            timePlayedSeconds += stat.getTimePlayed();
            totalScoredPoints += stat.getTotalScoredPoints();
            numberOfWonGames += stat.getNumberOfWonGames();
        }
        String timePlayed = TimeConverter.convertSecondsToTime(timePlayedSeconds);
        BigDecimal avgScore = avgScoreSummed.divide(BigDecimal.valueOf(stats.size()), RoundingMode.DOWN);
        return StatsWithUName.builder()
                .totalGamePlayed(totalGamePlayed)
                .avgScore(avgScore)
                .timePlayed(timePlayed)
                .totalScoredPoints(totalScoredPoints)
                .numberOfWonGames(numberOfWonGames)
                .build();
    }

    @Override
    public Map<Mode, StatsWithUName> getGameStatsOffAllUsersCombined() {
        return null;
    }
}
