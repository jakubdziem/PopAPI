package com.dziem.popapi.service;

import com.dziem.popapi.dto.webpage.*;
import com.dziem.popapi.formatter.TimeConverter;
import com.dziem.popapi.model.Mode;
import com.dziem.popapi.model.webpage.*;
import com.dziem.popapi.repository.*;
import lombok.AllArgsConstructor;
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
    private final StatsPageService statsPageService;
    private final DailyStatsSummedRepository dailyStatsSummedRepository;
    private final DailyUsersSummedRepository dailyUsersSummedRepository;
    private final ActiveUsersPageService activeUsersPageService;
    private final WeeklyActiveUsersRepository weeklyActiveUsersRepository;
    private final WeeklyNewUsersSummedRepository weeklyNewUsersSummedRepository;
    private final DailyActiveUsersRepository dailyActiveUsersRepository;

    @Override
    public void saveDailyStatsSummedSnapshot() {
        LocalDate day = LocalDate.now();
        DayOfWeek dayOfWeek = day.getDayOfWeek();
        StatsWithUNameDTO stats = statsPageService.getDifferenceStatsOfAllUsersCombined(ALL_TIME);
        Map<String, StatsWithUNameDTO> gameStats = statsPageService.getDifferenceGameStatsOffAllUsersCombined(ALL_TIME);
        StatsWithUNameDTO statsOfAllUsersCombinedCurrent = statsPageService.getStatsOfAllUsersCombinedCurrent();
        Map<String, StatsWithUNameDTO> gameStatsOffAllUsersCombinedCurrent = statsPageService.getGameStatsOffAllUsersCombinedCurrent();
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
                StatsWithUNameDTO currentStats;
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
    }
    @Override
    public void saveDailySummedUsersSnapshot() {
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        UsersSummedDTO differenceUsersSummedDTO = statsPageService.getDifferenceUsersSummed(ALL_TIME);
        if (!dayOfWeek.equals(DayOfWeek.SUNDAY)) {
            DailyUsersSummed dailyUsersSummedPrevDayInAWeek = getUsersSummedFromPrevDaysInAWeek(dayOfWeek, today);
            dailyUsersSummedRepository.save(DailyUsersSummed.builder()
                    .day(today)
                    .guestUsers(differenceUsersSummedDTO.getGuestUsers()-dailyUsersSummedPrevDayInAWeek.getGuestUsers())
                    .googleOrEmailUsers(differenceUsersSummedDTO.getGoogleOrEmailUsers()-dailyUsersSummedPrevDayInAWeek.getGoogleOrEmailUsers())
                    .build());
        } else {
            dailyUsersSummedRepository.save(DailyUsersSummed.builder()
                    .day(today)
                    .guestUsers(differenceUsersSummedDTO.getGuestUsers())
                    .googleOrEmailUsers(differenceUsersSummedDTO.getGoogleOrEmailUsers())
                    .build());
        }
    }

    @Override
    public void saveWeeklyNewUsersSummed() {
        UsersSummedDTO differenceUsersSummedDTO = statsPageService.getDifferenceUsersSummed(ALL_TIME);
        WeeklyNewUsersSummed weeklyNewUsersSummed = new WeeklyNewUsersSummed();
        weeklyNewUsersSummed.setWeekStartDate(LocalDate.now());
        weeklyNewUsersSummed.setNewUsers(differenceUsersSummedDTO.getGuestUsers() + differenceUsersSummedDTO.getGoogleOrEmailUsers());
        weeklyNewUsersSummedRepository.save(weeklyNewUsersSummed);
    }

    @Override
    public void saveWeeklyActiveUsers() {
        List<ActiveUsersStatsDTO> activeUsersStatsDTOThisWeek = activeUsersPageService.getActiveUsersStatsThisWeek();
        Integer size = activeUsersStatsDTOThisWeek.size();
        WeeklyActiveUsers weeklyActiveUsers = new WeeklyActiveUsers();
        weeklyActiveUsers.setWeekStartDate(LocalDate.now());
        weeklyActiveUsers.setActiveUsers(size);
        weeklyActiveUsers.setActiveNewUsers(activeUsersStatsDTOThisWeek.stream().filter(ActiveUsersStatsDTO::isNewUser).toList().size());
        weeklyActiveUsers.setActiveOldUsers(weeklyActiveUsers.getActiveUsers()-weeklyActiveUsers.getActiveNewUsers());
        weeklyActiveUsersRepository.save(weeklyActiveUsers);
    }

    @Override
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
        StatsWithUNameDTO stats = statsPageService.getDifferenceStatsOfAllUsersCombined(ALL_TIME);
        Map<String, StatsWithUNameDTO> gameStats = statsPageService.getDifferenceGameStatsOffAllUsersCombined(ALL_TIME);
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

    private DailyStatsSummed getDailyStatsSummedFromDayAndPerMode(StatsWithUNameDTO stats , int i, String mode, LocalDate week) {
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
        StatsWithUNameDTO stats = statsPageService.getStatsOfAllUsersCombinedFromWeek(LocalDate.parse("2024-11-10"));
        Map<String, StatsWithUNameDTO> gameStats = statsPageService.getGameStatsOffAllUsersCombinedFromWeek(LocalDate.parse("2024-11-10"));
        List<DailyStatsSummed> dailyStatsSummed = new ArrayList<>();
        for(int i = 1; i <= 7; i++) {
            dailyStatsSummed.add(getDailyStatsSummedFromDayAndPerMode(stats, i, COMBINED_STATS, week));
        }
        for(Mode modeEnum : Mode.values()) {
            String mode = modeEnum.toString();
            StatsWithUNameDTO gameStat = gameStats.get(mode);
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
    public List<DailyUsersSummedBothDTO> getDailyUsersSummedForChart() {
        List<DailyUsersSummed> dailyUsersSummedList = dailyUsersSummedRepository.getAll();
        List<DailyUsersSummedBothDTO> dailyUsersSummedBothDTOList = new ArrayList<>();
        for(DailyUsersSummed daily : dailyUsersSummedList) {
            dailyUsersSummedBothDTOList.add(DailyUsersSummedBothDTO.builder()
                    .day(daily.getDay())
                    .numberOfUsers(daily.getGuestUsers() + daily.getGoogleOrEmailUsers())
                    .build());
        }
        dailyUsersSummedBothDTOList.sort(Comparator.comparing(DailyUsersSummedBothDTO::getDay));
        return dailyUsersSummedBothDTOList;
    }

    @Override
    public DailyUsersSummed getTodayUserSummedDifference() {
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        UsersSummedDTO differenceUsersSummedDTO = statsPageService.getDifferenceUsersSummed(ALL_TIME);
        if(dayOfWeek == DayOfWeek.SUNDAY) {
            return DailyUsersSummed.builder()
                    .guestUsers(differenceUsersSummedDTO.getGuestUsers())
                    .googleOrEmailUsers(differenceUsersSummedDTO.getGoogleOrEmailUsers())
                    .build();
        }
        DailyUsersSummed dailyUsersSummedPrevDayInAWeek = getUsersSummedFromPrevDaysInAWeek(dayOfWeek, today);
        return DailyUsersSummed.builder()
                .guestUsers(differenceUsersSummedDTO.getGuestUsers() - dailyUsersSummedPrevDayInAWeek.getGuestUsers())
                .googleOrEmailUsers(differenceUsersSummedDTO.getGoogleOrEmailUsers() - dailyUsersSummedPrevDayInAWeek.getGoogleOrEmailUsers())
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
