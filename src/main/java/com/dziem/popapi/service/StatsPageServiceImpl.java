package com.dziem.popapi.service;

import com.dziem.popapi.model.Mode;
import com.dziem.popapi.model.ModeStats;
import com.dziem.popapi.model.Stats;
import com.dziem.popapi.model.User;
import com.dziem.popapi.model.webpage.*;
import com.dziem.popapi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
public class StatsPageServiceImpl implements StatsPageService {
    private final StatsRepository statsRepository;
    private final ModeStatsRepository modeStatsRepository;
    private final UserRepository userRepository;
    private final WeeklyStatsRepository weeklyStatsRepository;
    private final WeeklyUsersSummedRepository weeklyUsersSummedRepository;
    private final String COMBINED_STATS = "COMBINED_STATS";
    public final String STATS_OFF_ALL_USERS = "STATS_OFF_ALL_USERS";
    @Override
    public List<StatsWithUName> getStatsWithUNameOfAllUsersCurrent() {
        return getAllStatsWithUname(COMBINED_STATS);
    }

    @Override
    public Map<String, List<StatsWithUName>> getAllGameStatsWithUNameOfAllUsersCurrent() {
        Map<String, List<StatsWithUName>> modeStatsWithUNameHashMap = new HashMap<>();
        for(Mode mode : Mode.values()) {
            List<StatsWithUName> modeStatsWithUNameByCertainMode = getAllStatsWithUname(mode.toString());
            modeStatsWithUNameHashMap.put(mode.toString(), modeStatsWithUNameByCertainMode);
        }
        return modeStatsWithUNameHashMap;
    }

    @Override
    public StatsWithUName getStatsOfAllUsersCombinedCurrent() {
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
    public Map<String, StatsWithUName> getGameStatsOffAllUsersCombinedCurrent() {
        Map<String, StatsWithUName> modeStatsWithUNameHashMap = new HashMap<>();
        Map<String, List<ModeStats>> modeStatsGroupedByMode = modeStatsRepository.findAll().stream().collect(groupingBy(ModeStats::getMode));

        for(String mode : modeStatsGroupedByMode.keySet()) {
            Long totalGamePlayed = 0L;
            BigDecimal avgScoreSummed = BigDecimal.ZERO;
            Long timePlayedSeconds = 0L;
            Long totalScoredPoints = 0L;
            Integer numberOfWonGames = 0;
            List<ModeStats> modeStatsListOfCertainMode = modeStatsGroupedByMode.get(mode);
            for(ModeStats modeStat : modeStatsListOfCertainMode) {
                totalGamePlayed += modeStat.getTotalGamePlayed();
                avgScoreSummed = avgScoreSummed.add(modeStat.getAvgScore());
                timePlayedSeconds += modeStat.getTimePlayed();
                totalScoredPoints += modeStat.getTotalScoredPoints();
                numberOfWonGames += modeStat.getNumberOfWonGames();
            }
            String timePlayed = TimeConverter.convertSecondsToTime(timePlayedSeconds);
            BigDecimal avgScore = avgScoreSummed.divide(BigDecimal.valueOf(modeStatsListOfCertainMode.size()), RoundingMode.DOWN);
            StatsWithUName singleModeStat = StatsWithUName.builder()
                    .totalGamePlayed(totalGamePlayed)
                    .avgScore(avgScore)
                    .timePlayed(timePlayed)
                    .totalScoredPoints(totalScoredPoints)
                    .numberOfWonGames(numberOfWonGames)
                    .build();
            modeStatsWithUNameHashMap.put(mode, singleModeStat);
        }
        return modeStatsWithUNameHashMap;
    }

    @Override
    public UsersSummed getUsersSummedCurrent() {
        Map<Boolean, List<User>> usersDistiguishedPerGuestBoolean = userRepository.findAll().stream().collect(groupingBy(User::isGuest));
        List<User> guests = usersDistiguishedPerGuestBoolean.get(true);
        List<User> googleOrEmailUsers = usersDistiguishedPerGuestBoolean.get(false);
        UsersSummed usersSummed = new UsersSummed();
        usersSummed.setGuestUsers(guests.size());
        usersSummed.setGoogleOrEmailUsers(googleOrEmailUsers.size());
        return usersSummed;
    }

    private List<StatsWithUName> getAllStatsWithUname(String mode) {
        List<Object[]> result;
        boolean contains = false;
        for(Mode modeEnum : Mode.values()) {
            if(modeEnum.toString().equals(mode)) {
                contains = true;
                break;
            }
        }
        if(mode.equals(COMBINED_STATS)) {
            result = statsRepository.getStatsWithUNameRaw();
        } else if(contains) {
            result = modeStatsRepository.getModeStatsWithUNameRaw(mode);
        } else {
            return new ArrayList<>();
        }
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

    @Override
    public List<StatsWithUName> getStatsWithUNameOfAllUsersFromWeek(LocalDate week) {
        List<WeeklyStats> weeklyStats = weeklyStatsRepository.getStatsWithUNameOfAllUsersFromWeek(week);
        List<StatsWithUName> statsWithUNames = new ArrayList<>();
        for(WeeklyStats stat : weeklyStats) {
            statsWithUNames.add(StatsWithUName.builder()
                    .userId(stat.getUserId())
                    .totalGamePlayed(stat.getTotalGamePlayed())
                    .avgScore(stat.getAvgScore())
                    .timePlayed(stat.getTimePlayed())
                    .totalScoredPoints(stat.getTotalScoredPoints())
                    .numberOfWonGames(stat.getNumberOfWonGames())
                    .name(stat.getName())
                    .build());
        }
        return statsWithUNames;
    }

    @Override
    public Map<String, List<StatsWithUName>> getAllGameStatsWithUNameOfAllUsersFromWeek(LocalDate week) {
        List<WeeklyStats> weeklyStats = weeklyStatsRepository.getAllGameStatsWithUNameOfAllUsersFromWeek(week);
        Map<String, List<StatsWithUName>> gameStatsWithUnameMap = new HashMap<>();
        for(Mode mode : Mode.values()) {
            List<StatsWithUName> gameStatsWithUnames = new ArrayList<>();
            List<WeeklyStats> filteredWeeklyStatsFromLoopedMode = weeklyStats.stream().filter(weeklyStat -> weeklyStat.getMode().equals(mode.toString())).toList();
            for(WeeklyStats weeklyStat : filteredWeeklyStatsFromLoopedMode) {
                gameStatsWithUnames.add(StatsWithUName.builder()
                        .userId(weeklyStat.getUserId())
                        .totalGamePlayed(weeklyStat.getTotalGamePlayed())
                        .avgScore(weeklyStat.getAvgScore())
                        .timePlayed(weeklyStat.getTimePlayed())
                        .totalScoredPoints(weeklyStat.getTotalScoredPoints())
                        .numberOfWonGames(weeklyStat.getNumberOfWonGames())
                        .name(weeklyStat.getName())
                        .build());
            }
            gameStatsWithUnameMap.put(mode.toString(), gameStatsWithUnames);
        }
        return gameStatsWithUnameMap;
    }

    @Override
    public StatsWithUName getStatsOfAllUsersCombinedFromWeek(LocalDate week) {
        WeeklyStats weeklyStats = weeklyStatsRepository.getStatsOfAllUsersCombinedFromWeek(week);
        return StatsWithUName.builder()
                .totalGamePlayed(weeklyStats.getTotalGamePlayed())
                .avgScore(weeklyStats.getAvgScore())
                .timePlayed(weeklyStats.getTimePlayed())
                .totalScoredPoints(weeklyStats.getTotalScoredPoints())
                .numberOfWonGames(weeklyStats.getNumberOfWonGames())
                .build();
    }

    @Override
    public Map<String, StatsWithUName> getGameStatsOffAllUsersCombinedFromWeek(LocalDate week) {
        List<WeeklyStats> allGameStatsWithUNameOfAllUsersFromWeek = weeklyStatsRepository.getGameStatsOffAllUsersCombinedFromWeek(week);
        Map<String, StatsWithUName> statsWithUNameMap = new HashMap<>();
        for(WeeklyStats weeklyStats : allGameStatsWithUNameOfAllUsersFromWeek) {
            statsWithUNameMap.put(weeklyStats.getMode(), StatsWithUName.builder()
                    .totalGamePlayed(weeklyStats.getTotalGamePlayed())
                    .avgScore(weeklyStats.getAvgScore())
                    .timePlayed(weeklyStats.getTimePlayed())
                    .totalScoredPoints(weeklyStats.getTotalScoredPoints())
                    .numberOfWonGames(weeklyStats.getNumberOfWonGames())
                    .build());
        }
        return statsWithUNameMap;
    }

    @Override
    public UsersSummed getUsersSummedFromWeek(LocalDate week) {
        AtomicReference<UsersSummed> atomicReference = new AtomicReference<>();
        weeklyUsersSummedRepository.findById(week)
                .ifPresentOrElse(
                        weeklyUsersSummed -> {
                            UsersSummed usersSummed = new UsersSummed();
                            usersSummed.setGuestUsers(weeklyUsersSummed.getGuestUsers());
                            usersSummed.setGoogleOrEmailUsers(weeklyUsersSummed.getGoogleOrEmailUsers());
                            atomicReference.set(usersSummed);
                        },
                        () -> atomicReference.set(null)
                );
        return atomicReference.get() == null ? new UsersSummed() : atomicReference.get();
    }
    @Scheduled(cron = "0 0 0 * * SUN")
    @Override
    public void saveWeeklyStatsSnapshot() {
        LocalDate week = LocalDate.now();
        List<WeeklyStats> weeklyStats = new ArrayList<>();

        List<StatsWithUName> statsWithUNameOfAllUsersCurrent = getStatsWithUNameOfAllUsersCurrent();
        for(StatsWithUName stat : statsWithUNameOfAllUsersCurrent) {
            weeklyStats.add(WeeklyStats.builder()
                    .weekStartDate(week)
                    .userId(stat.getUserId())
                    .mode(COMBINED_STATS)
                    .totalGamePlayed(stat.getTotalGamePlayed())
                    .avgScore(stat.getAvgScore())
                    .timePlayed(stat.getTimePlayed())
                    .totalScoredPoints(stat.getTotalScoredPoints())
                    .numberOfWonGames(stat.getNumberOfWonGames())
                    .name(stat.getName())
                    .build());
        }

        Map<String, List<StatsWithUName>> allGameStatsWithUNameOfAllUsersCurrent = getAllGameStatsWithUNameOfAllUsersCurrent();
        for(String mode : allGameStatsWithUNameOfAllUsersCurrent.keySet()) {
            for(StatsWithUName stat : allGameStatsWithUNameOfAllUsersCurrent.get(mode)) {
                weeklyStats.add(WeeklyStats.builder()
                        .weekStartDate(week)
                        .userId(stat.getUserId())
                        .mode(mode)
                        .totalGamePlayed(stat.getTotalGamePlayed())
                        .avgScore(stat.getAvgScore())
                        .timePlayed(stat.getTimePlayed())
                        .totalScoredPoints(stat.getTotalScoredPoints())
                        .numberOfWonGames(stat.getNumberOfWonGames())
                        .name(stat.getName())
                        .build());
            }
        }

        StatsWithUName statsOfAllUsersCombinedCurrent = getStatsOfAllUsersCombinedCurrent();
        weeklyStats.add(WeeklyStats.builder()
                .weekStartDate(week)
                .userId(STATS_OFF_ALL_USERS)
                .mode(COMBINED_STATS)
                .totalGamePlayed(statsOfAllUsersCombinedCurrent.getTotalGamePlayed())
                .avgScore(statsOfAllUsersCombinedCurrent.getAvgScore())
                .timePlayed(statsOfAllUsersCombinedCurrent.getTimePlayed())
                .totalScoredPoints(statsOfAllUsersCombinedCurrent.getTotalScoredPoints())
                .numberOfWonGames(statsOfAllUsersCombinedCurrent.getNumberOfWonGames())
                .name(null)
                .build());

        Map<String, StatsWithUName> gameStatsOffAllUsersCombinedCurrent = getGameStatsOffAllUsersCombinedCurrent();
        for(String mode : gameStatsOffAllUsersCombinedCurrent.keySet()) {
            StatsWithUName stat = gameStatsOffAllUsersCombinedCurrent.get(mode);
            weeklyStats.add(WeeklyStats.builder()
                    .weekStartDate(week)
                    .userId(STATS_OFF_ALL_USERS + mode)
                    .mode(mode)
                    .totalGamePlayed(stat.getTotalGamePlayed())
                    .avgScore(stat.getAvgScore())
                    .timePlayed(stat.getTimePlayed())
                    .totalScoredPoints(stat.getTotalScoredPoints())
                    .numberOfWonGames(stat.getNumberOfWonGames())
                    .name(null)
                    .build());
        }


        weeklyStatsRepository.saveAll(weeklyStats);

        UsersSummed usersSummedCurrent = getUsersSummedCurrent();
        WeeklyUsersSummed weeklyUsersSummed = WeeklyUsersSummed.builder()
                .weekStartDate(week)
                .guestUsers(usersSummedCurrent.getGuestUsers())
                .googleOrEmailUsers(usersSummedCurrent.getGoogleOrEmailUsers())
                .build();
        weeklyUsersSummedRepository.save(weeklyUsersSummed);
    }

    @Override
    public List<LocalDate> getWeeks() {
        return weeklyStatsRepository.getWeeks();
    }
}
