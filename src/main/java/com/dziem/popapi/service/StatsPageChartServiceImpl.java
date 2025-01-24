package com.dziem.popapi.service;

import com.dziem.popapi.model.Mode;
import com.dziem.popapi.model.webpage.*;
import com.dziem.popapi.repository.*;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import static com.dziem.popapi.controller.StatsPageController.ALL_TIME;
import static com.dziem.popapi.service.StatsPageServiceImpl.COMBINED_STATS;
import static java.util.stream.Collectors.groupingBy;

@Service
@AllArgsConstructor
public class StatsPageChartServiceImpl implements StatsPageChartService {
    private static final Logger logger = LoggerFactory.getLogger(StatsPageServiceImpl.class);
    private final StatsPageService statsPageService;
    private final DailyStatsSummedRepository dailyStatsSummedRepository;
    private final DailyUsersSummedRepository dailyUsersSummedRepository;
    private final ActiveUsersPageService activeUsersPageService;
    private final WeeklyActiveUsersRepository weeklyActiveUsersRepository;
    private final WeeklyNewUsersSummedRepository weeklyNewUsersSummedRepository;
    private final DailyActiveUsersRepository dailyActiveUsersRepository;

    @Scheduled(cron = "0 30 23 * * *", zone = "Europe/Warsaw")
    @Override
    public void saveDailyStatsSummedSnapshot() {
        logger.info("Starting daily summed stats snapshot...");
        LocalDate day = LocalDate.now();
        DayOfWeek dayOfWeek = day.getDayOfWeek();
        StatsWithUName stats = statsPageService.getDifferenceStatsOfAllUsersCombined(ALL_TIME);
        Map<String, StatsWithUName> gameStats = statsPageService.getDifferenceGameStatsOffAllUsersCombined(ALL_TIME);
        StatsWithUName statsOfAllUsersCombinedCurrent = statsPageService.getStatsOfAllUsersCombinedCurrent();
        Map<String, StatsWithUName> gameStatsOffAllUsersCombinedCurrent = statsPageService.getGameStatsOffAllUsersCombinedCurrent();
        List<DailyStatsSummed> dailyStats = new ArrayList<>();
        if (!dayOfWeek.equals(DayOfWeek.SUNDAY)) {
            Map<String, DailyStatsSummed> dailyStatsSummedInCurrentWeek = new HashMap<>();
            List<String> modes = new ArrayList<>(Arrays.stream(Mode.values()).map(Enum::toString).toList());
            modes.add(COMBINED_STATS);
            for(String mode : modes) {
                DailyStatsSummed dailyStatsSummed = DailyStatsSummed.builder()
                        .mode(mode)
                        .totalGamePlayed(0L)
                        .totalScoredPoints(0L)
                        .avgScore(BigDecimal.ZERO)
                        .numberOfWonGames(0)
                        .timePlayed("00:00:00").build();
                dailyStatsSummedInCurrentWeek.put(mode,dailyStatsSummed);
            }

            for(int i = dayOfWeek.getValue(); i >= 1; i--) {
                List<DailyStatsSummed> dailyStatsSummedPrevDayList = dailyStatsSummedRepository.findAllByDay(day.minusDays(i));
                Map<String, List<DailyStatsSummed>> dailyStatsSummedPrevDayMap = dailyStatsSummedPrevDayList.stream().collect(groupingBy(DailyStatsSummed::getMode));
                for (String mode : dailyStatsSummedPrevDayMap.keySet()) {
                    DailyStatsSummed dailyStatsSummedPrevDay = dailyStatsSummedInCurrentWeek.get(mode);
                    dailyStatsSummedPrevDay.setTotalGamePlayed(dailyStatsSummedPrevDayMap.get(mode).get(0).getTotalGamePlayed()
                            + dailyStatsSummedPrevDay.getTotalGamePlayed());
                    dailyStatsSummedPrevDay.setTimePlayed(TimeConverter.sumOfTime(dailyStatsSummedPrevDayMap.get(mode).get(0).getTimePlayed(),
                            dailyStatsSummedPrevDay.getTimePlayed()));
                    dailyStatsSummedPrevDay.setTotalScoredPoints(dailyStatsSummedPrevDayMap.get(mode).get(0).getTotalScoredPoints()
                            + dailyStatsSummedPrevDay.getTotalScoredPoints());
                    dailyStatsSummedPrevDay.setNumberOfWonGames(dailyStatsSummedPrevDayMap.get(mode).get(0).getNumberOfWonGames()
                            + dailyStatsSummedPrevDay.getNumberOfWonGames());
                    dailyStatsSummedInCurrentWeek.put(mode, dailyStatsSummedPrevDay);
                }
            }
            for(String mode : dailyStatsSummedInCurrentWeek.keySet()) {
                DailyStatsSummed dailyStatsSummed = dailyStatsSummedInCurrentWeek.get(mode);
                StatsWithUName currentStats;
                if(mode.equals(COMBINED_STATS)) {
                    currentStats = stats;
                } else {
                    currentStats = gameStats.get(mode);
                }
                dailyStats.add(DailyStatsSummed.builder()
                        .day(day)
                        .mode(mode)
                        .totalGamePlayed(Math.max(currentStats.getTotalGamePlayed()-dailyStatsSummed.getTotalGamePlayed(),0))
                        .avgScore(mode.equals(COMBINED_STATS) ? statsOfAllUsersCombinedCurrent.getAvgScore() : gameStatsOffAllUsersCombinedCurrent.get(mode).getAvgScore())
                        .timePlayed(TimeConverter.differenceOfTime(currentStats.getTimePlayed(), dailyStatsSummed.getTimePlayed()).equals("00:00:00") ? "00:00:00"
                                : TimeConverter.differenceOfTime(currentStats.getTimePlayed(), dailyStatsSummed.getTimePlayed()))
                        .totalScoredPoints(Math.max(currentStats.getTotalScoredPoints()-dailyStatsSummed.getTotalScoredPoints(),0))
                        .numberOfWonGames(Math.max(currentStats.getNumberOfWonGames() - dailyStatsSummed.getNumberOfWonGames(),0))
                        .build());
            }
        } else {
            dailyStats.add(DailyStatsSummed.builder()
                    .day(day)
                    .mode(COMBINED_STATS)
                    .totalGamePlayed(stats.getTotalGamePlayed())
                    .avgScore(statsOfAllUsersCombinedCurrent.getAvgScore())
                    .timePlayed(stats.getTimePlayed())
                    .totalScoredPoints(stats.getTotalScoredPoints())
                    .numberOfWonGames(stats.getNumberOfWonGames())
                    .build());
            for (String mode : gameStats.keySet()) {
                dailyStats.add(DailyStatsSummed.builder()
                        .day(day)
                        .mode(mode)
                        .totalGamePlayed(gameStats.get(mode).getTotalGamePlayed())
                        .avgScore(gameStatsOffAllUsersCombinedCurrent.get(mode).getAvgScore())
                        .timePlayed(gameStats.get(mode).getTimePlayed())
                        .totalScoredPoints(gameStats.get(mode).getTotalScoredPoints())
                        .numberOfWonGames(gameStats.get(mode).getNumberOfWonGames())
                        .build());
            }
        }
        dailyStatsSummedRepository.saveAll(dailyStats);
        logger.info("Daily summed stats snapshot completed.");
    }
    @Scheduled(cron = "0 30 23 * * *", zone = "Europe/Warsaw")
    @Override
    public void saveDailySummedUsersSnapshot() {
        logger.info("Starting daily summed users snapshot...");
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        UsersSummed differenceUsersSummed = statsPageService.getDifferenceUsersSummed(ALL_TIME);
        if (!dayOfWeek.equals(DayOfWeek.SUNDAY)) {
            DailyUsersSummed dailyUsersSummedPrevDayInAWeek = getUsersSummedFromPrevDaysInAWeek(dayOfWeek, today);
            dailyUsersSummedRepository.save(DailyUsersSummed.builder()
                    .day(today)
                    .guestUsers(differenceUsersSummed.getGuestUsers()-dailyUsersSummedPrevDayInAWeek.getGuestUsers())
                    .googleOrEmailUsers(differenceUsersSummed.getGoogleOrEmailUsers()-dailyUsersSummedPrevDayInAWeek.getGoogleOrEmailUsers())
                    .build());
        } else {
            dailyUsersSummedRepository.save(DailyUsersSummed.builder()
                    .day(today)
                    .guestUsers(differenceUsersSummed.getGuestUsers())
                    .googleOrEmailUsers(differenceUsersSummed.getGoogleOrEmailUsers())
                    .build());
        }
        logger.info("Daily summed users snapshot completed.");
    }

    @Override
    @Scheduled(cron = "0 55 5 * * SUN", zone = "Europe/Warsaw")
    public void saveWeeklyNewUsersSummed() {
        UsersSummed differenceUsersSummed = statsPageService.getDifferenceUsersSummed(ALL_TIME);
        WeeklyNewUsersSummed weeklyNewUsersSummed = new WeeklyNewUsersSummed();
        weeklyNewUsersSummed.setWeekStartDate(LocalDate.now());
        weeklyNewUsersSummed.setNewUsers(differenceUsersSummed.getGuestUsers() + differenceUsersSummed.getGoogleOrEmailUsers());
        weeklyNewUsersSummedRepository.save(weeklyNewUsersSummed);
    }

    @Override
    @Scheduled(cron = "0 55 5 * * SUN", zone = "Europe/Warsaw")
    public void saveWeeklyActiveUsers() {
        List<ActiveUsersStats> activeUsersStatsThisWeek = activeUsersPageService.getActiveUsersStatsThisWeek();
        Integer size = activeUsersStatsThisWeek.size();
        WeeklyActiveUsers weeklyActiveUsers = new WeeklyActiveUsers();
        weeklyActiveUsers.setWeekStartDate(LocalDate.now());
        weeklyActiveUsers.setActiveUsers(size);
        weeklyActiveUsers.setActiveNewUsers(activeUsersStatsThisWeek.stream().filter(ActiveUsersStats::isNew).toList().size());
        weeklyActiveUsers.setActiveOldUsers(weeklyActiveUsers.getActiveUsers()-weeklyActiveUsers.getActiveNewUsers());
        weeklyActiveUsersRepository.save(weeklyActiveUsers);
    }

    @Override
    @Scheduled(cron = "0 30 23 * * *", zone = "Europe/Warsaw") //start scheduling at sunday
    public void saveDailyActiveUsers() {
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        DailyActiveUsers dailyActiveUsers = new DailyActiveUsers();
        dailyActiveUsers.setDay(today);
        Integer sizeToday = activeUsersPageService.getActiveUsersStatsThisWeek().size();
        if(dayOfWeek == DayOfWeek.SUNDAY) {
             dailyActiveUsers.setActiveUsers(sizeToday);
        } else {
            Integer sumOfPrevActiveUsersThisWeek = 0;
            for(int i = dayOfWeek.getValue(); i >= 1; i--) {
                LocalDate prevDay = LocalDate.now().minusDays(i);
                DailyActiveUsers dailyActiveUsersPrevDay = dailyActiveUsersRepository
                        .findById(prevDay).orElseGet(
                                () -> {
                                    DailyActiveUsers d = new DailyActiveUsers();
                                    d.setActiveUsers(0);
                                    d.setDay(prevDay);
                                    return d;
                                }
                        );
                sumOfPrevActiveUsersThisWeek += dailyActiveUsersPrevDay.getActiveUsers();
            }
            dailyActiveUsers.setActiveUsers(activeUsersPageService.getActiveUsersStatsThisWeek().size() - sumOfPrevActiveUsersThisWeek);
        }
        dailyActiveUsersRepository.save(dailyActiveUsers);
    }

    @Override
    public void saveDailySummedStatsFirst() {
        StatsWithUName stats = statsPageService.getDifferenceStatsOfAllUsersCombined(ALL_TIME);
        Map<String, StatsWithUName> gameStats = statsPageService.getDifferenceGameStatsOffAllUsersCombined(ALL_TIME);
        List<DailyStatsSummed> dailyStats = new ArrayList<>();
        LocalDate day = LocalDate.now();
        dailyStats.add(DailyStatsSummed.builder()
                .day(day)
                .mode(COMBINED_STATS)
                .totalGamePlayed(stats.getTotalGamePlayed())
                .avgScore(stats.getAvgScore())
                .timePlayed(stats.getTimePlayed())
                .totalScoredPoints(stats.getTotalScoredPoints())
                .numberOfWonGames(stats.getNumberOfWonGames())
                .build());
        for (String mode : gameStats.keySet()) {
            dailyStats.add(DailyStatsSummed.builder()
                    .day(day)
                    .mode(mode)
                    .totalGamePlayed(gameStats.get(mode).getTotalGamePlayed())
                    .avgScore(gameStats.get(mode).getAvgScore())
                    .timePlayed(gameStats.get(mode).getTimePlayed())
                    .totalScoredPoints(gameStats.get(mode).getTotalScoredPoints())
                    .numberOfWonGames(gameStats.get(mode).getNumberOfWonGames())
                    .build());
        }
        dailyStatsSummedRepository.saveAll(dailyStats);
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


    //note data before 2024-11-18 was calculated by function not saved daily like the other data
    @Override
    public List<DailyStatsSummed> getDailyStatsSummedForChartPerMode(String mode) {
        Stream<DailyStatsSummed> allTrueDataStream = dailyStatsSummedRepository.findAll().stream()
                .filter(d -> d.getDay().isAfter(LocalDate.of(2024, 11, 17)));
        return allTrueDataStream.filter(stats -> stats.getMode().equals(mode))
                .sorted(Comparator.comparing(DailyStatsSummed::getDay)).toList();
    }
    //note data before 2024-11-18 was calculated by function not saved daily like the other data
    @Override
    public List<DailyUsersSummedBoth> getDailyUsersSummedForChart() {
        List<DailyUsersSummed> dailyUsersSummedList = dailyUsersSummedRepository.getAll();
        List<DailyUsersSummedBoth> dailyUsersSummedBothList = new ArrayList<>();
        for(DailyUsersSummed daily : dailyUsersSummedList) {
            dailyUsersSummedBothList.add(DailyUsersSummedBoth.builder()
                    .day(daily.getDay())
                    .numberOfUsers(daily.getGuestUsers() + daily.getGoogleOrEmailUsers())
                    .build());
        }
        dailyUsersSummedBothList.sort(Comparator.comparing(DailyUsersSummedBoth::getDay));
        return dailyUsersSummedBothList;
    }

    @Override
    public DailyUsersSummed getTodayUserSummedDifference() {
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        UsersSummed differenceUsersSummed = statsPageService.getDifferenceUsersSummed(ALL_TIME);
        if(dayOfWeek == DayOfWeek.SUNDAY) {
            return DailyUsersSummed.builder()
                    .guestUsers(differenceUsersSummed.getGuestUsers())
                    .googleOrEmailUsers(differenceUsersSummed.getGoogleOrEmailUsers())
                    .build();
        }
        DailyUsersSummed dailyUsersSummedPrevDayInAWeek = getUsersSummedFromPrevDaysInAWeek(dayOfWeek, today);
        return DailyUsersSummed.builder()
                .guestUsers(differenceUsersSummed.getGuestUsers() - dailyUsersSummedPrevDayInAWeek.getGuestUsers())
                .googleOrEmailUsers(differenceUsersSummed.getGoogleOrEmailUsers() - dailyUsersSummedPrevDayInAWeek.getGoogleOrEmailUsers())
                .build();
    }

    @Override
    public List<DailyActiveUsers> getDailyActiveUsersForChart() {
        List<DailyActiveUsers> dailyActiveUsersSorted = new ArrayList<>(dailyActiveUsersRepository.findAll().stream()
                .filter(d -> d.getDay().isAfter(LocalDate.of(2024, 12, 12))).toList());
        dailyActiveUsersSorted.sort(Comparator.comparing(DailyActiveUsers::getDay));
        return dailyActiveUsersSorted;
    }

    @Override
    public List<WeeklyNewUsersSummed> getWeeklyNewUsersSummedForChart() {
        List<WeeklyNewUsersSummed> weeklyNewUsersSummedSorted = weeklyNewUsersSummedRepository.findAll();
        weeklyNewUsersSummedSorted.sort(Comparator.comparing(WeeklyNewUsersSummed::getWeekStartDate));
        return weeklyNewUsersSummedSorted;
    }

    @Override
    public List<WeeklyActiveUsers> getWeeklyActiveUsersForChart() {
        List<WeeklyActiveUsers> weeklyActiveUsersSorted = weeklyActiveUsersRepository.findAll();
        weeklyActiveUsersSorted.sort(Comparator.comparing(WeeklyActiveUsers::getWeekStartDate));
        return weeklyActiveUsersSorted;
    }

    private DailyUsersSummed getUsersSummedFromPrevDaysInAWeek(DayOfWeek dayOfWeek, LocalDate today) {
        DailyUsersSummed dailyUsersSummedPrevDayInAWeek = new DailyUsersSummed();
        for(int i = dayOfWeek.getValue(); i >= 1; i--) {
            DailyUsersSummed dailyUsersSummedPrevDay = dailyUsersSummedRepository.findById(today.minusDays(i))
                    .orElse(DailyUsersSummed.builder()
                            .day(today.minusDays(i))
                            .guestUsers(0)
                            .googleOrEmailUsers(0)
                            .build());
            dailyUsersSummedPrevDayInAWeek.setGuestUsers(dailyUsersSummedPrevDayInAWeek.getGuestUsers()
                    + dailyUsersSummedPrevDay.getGuestUsers());
            dailyUsersSummedPrevDayInAWeek.setGoogleOrEmailUsers(dailyUsersSummedPrevDayInAWeek.getGoogleOrEmailUsers()
                    + dailyUsersSummedPrevDay.getGoogleOrEmailUsers());
        }
        return dailyUsersSummedPrevDayInAWeek;
    }
}
