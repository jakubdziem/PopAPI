package com.dziem.popapi.service;

import com.dziem.popapi.model.Mode;
import com.dziem.popapi.model.webpage.DailyStatsSummed;
import com.dziem.popapi.model.webpage.StatsWithUName;
import com.dziem.popapi.model.webpage.TimeConverter;
import com.dziem.popapi.repository.DailyStatsSummedRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.dziem.popapi.controller.StatsPageController.ALL_TIME;
import static com.dziem.popapi.service.StatsPageServiceImpl.COMBINED_STATS;

@Service
@AllArgsConstructor
public class StatsPageChartServiceImpl implements StatsPageChartService {
    private static final Logger logger = LoggerFactory.getLogger(StatsPageServiceImpl.class);
    private final StatsPageService statsPageService;
    private final DailyStatsSummedRepository dailyStatsSummedRepository;
    @Scheduled(cron = "0 0 0 * * *")
    @Override
    public void saveDailySummedStatsSnapshot() {
        logger.info("Starting daily summed stats snapshot...");
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        StatsWithUName stats = statsPageService.getDifferenceStatsOfAllUsersCombined(ALL_TIME);
        Map<String, StatsWithUName> gameStats = statsPageService.getDifferenceGameStatsOffAllUsersCombined(ALL_TIME);
        List<DailyStatsSummed> dailyStats = new ArrayList<>();
        LocalDate day = LocalDate.now();
        if(!dayOfWeek.equals(DayOfWeek.SUNDAY)) {
            List<DailyStatsSummed> dailySummedStatsPrevDay = dailyStatsSummedRepository.findAllByDay(day.minusDays(1));
            DailyStatsSummed combinedStatsPrevDay = dailySummedStatsPrevDay.stream().filter(stat -> stat.getMode().equals(COMBINED_STATS)).toList().get(0);
            dailyStats.add(DailyStatsSummed.builder()
                    .day(day)
                    .mode(COMBINED_STATS)
                    .totalGamePlayed(Math.max(stats.getTotalGamePlayed() - combinedStatsPrevDay.getTotalGamePlayed(),0))
                    .avgScore(stats.getAvgScore().subtract(combinedStatsPrevDay.getAvgScore()).compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO :
                            stats.getAvgScore().subtract(combinedStatsPrevDay.getAvgScore()))
                    .timePlayed(TimeConverter.differenceOfTime(stats.getTimePlayed(), combinedStatsPrevDay.getTimePlayed()).equals("00:00:00") ? "00:00:00" :
                            TimeConverter.differenceOfTime(stats.getTimePlayed(), combinedStatsPrevDay.getTimePlayed()))
                    .totalScoredPoints(Math.max(stats.getTotalScoredPoints() - combinedStatsPrevDay.getTotalScoredPoints(),0))
                    .numberOfWonGames(Math.max(stats.getNumberOfWonGames() - combinedStatsPrevDay.getNumberOfWonGames(), 0))
                    .build());
            for(String mode : gameStats.keySet()) {
                combinedStatsPrevDay = dailySummedStatsPrevDay.stream().filter(stat -> stat.getMode().equals(mode)).toList().get(0);
                dailyStats.add(DailyStatsSummed.builder()
                        .day(day)
                        .mode(mode)
                        .totalGamePlayed(Math.max(gameStats.get(mode).getTotalGamePlayed() - combinedStatsPrevDay.getTotalGamePlayed(),0))
                        .avgScore(gameStats.get(mode).getAvgScore().subtract(combinedStatsPrevDay.getAvgScore()).compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO :
                                gameStats.get(mode).getAvgScore().subtract(combinedStatsPrevDay.getAvgScore()))
                        .timePlayed(TimeConverter.differenceOfTime(gameStats.get(mode).getTimePlayed(), combinedStatsPrevDay.getTimePlayed()).equals("00:00:00") ? "00:00:00" :
                                TimeConverter.differenceOfTime(gameStats.get(mode).getTimePlayed(), combinedStatsPrevDay.getTimePlayed()))
                        .totalScoredPoints(Math.max(gameStats.get(mode).getTotalScoredPoints() - combinedStatsPrevDay.getTotalScoredPoints(),0))
                        .numberOfWonGames(Math.max(gameStats.get(mode).getNumberOfWonGames() - combinedStatsPrevDay.getNumberOfWonGames(), 0))
                        .build());
            }
        }
        dailyStatsSummedRepository.saveAll(dailyStats);
        logger.info("Daily summed stats snapshot completed.");
    }

    private DailyStatsSummed getDailyStatsSummedFromDayAndPerMode(StatsWithUName stats ,int i, String mode, LocalDate week) {
        long restTotalGamePlayed = stats.getTotalGamePlayed() % 7;
        long totalGamePlayedPerDay = stats.getTotalGamePlayed() / 7;
        long seconds = TimeConverter.getSeconds(stats.getTimePlayed().split(":"));
        long restTimePlayed = seconds % 7;
        long totalTimePlayedPerDay = seconds / 7;
        long restScoredPoints = stats.getTotalScoredPoints() % 7;
        long totalScoredPointsPerDay = stats.getTotalScoredPoints() / 7;
        Integer numberOfWonGames = stats.getNumberOfWonGames();
        return DailyStatsSummed.builder()
                .day(week.minusDays(i))
                .mode(mode)
                .totalGamePlayed(i != 7 ? totalGamePlayedPerDay : totalGamePlayedPerDay + restTotalGamePlayed)
                .timePlayed(i != 7 ? TimeConverter.convertSecondsToTime(totalTimePlayedPerDay)
                        : TimeConverter.convertSecondsToTime(totalGamePlayedPerDay + restTimePlayed))
                .totalScoredPoints(i != 7 ? totalScoredPointsPerDay : totalScoredPointsPerDay + restScoredPoints)
                .numberOfWonGames(i != 7 ? 0 : numberOfWonGames)
                .avgScore(stats.getAvgScore().subtract(new BigDecimal(String.format("0.0%d", i))))
                .build();
    }
    @Override
    public void populateWeek(LocalDate week) {
        StatsWithUName stats = statsPageService.getStatsOfAllUsersCombinedFromWeek(LocalDate.parse("2024-11-10"));
        Map<String, StatsWithUName> gameStats = statsPageService.getGameStatsOffAllUsersCombinedFromWeek(LocalDate.parse("2024-11-10"));
        List<DailyStatsSummed> dailyStatsSummed = new ArrayList<>();
        for(int i = 1; i <= 7; i++) {
            dailyStatsSummed.add(getDailyStatsSummedFromDayAndPerMode(stats, i, COMBINED_STATS, week));
        }
        for(Mode modeEnum : Mode.values()) {
            String mode = modeEnum.toString();
            StatsWithUName gameStat = gameStats.get(mode);
            for(int i = 1 ; i <= 7; i++) {
                dailyStatsSummed.add(getDailyStatsSummedFromDayAndPerMode(gameStat, i, mode, week));
            }
        }
        dailyStatsSummedRepository.saveAll(dailyStatsSummed);
    }


    @Override
    public List<DailyStatsSummed> getDailyStatsSummedForChartPerMode(String mode) {
        return dailyStatsSummedRepository.findAll().stream().filter(stats -> stats.getMode().equals(mode))
                .sorted(Comparator.comparing(DailyStatsSummed::getDay)).toList();
    }
}
