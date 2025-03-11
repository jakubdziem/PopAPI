package com.dziem.popapi.service;

import com.dziem.popapi.model.Mode;
import com.dziem.popapi.model.ModeStats;
import com.dziem.popapi.model.Stats;
import com.dziem.popapi.model.User;
import com.dziem.popapi.model.webpage.*;
import com.dziem.popapi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.dziem.popapi.controller.StatsPageController.ALL_TIME;
import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
public class StatsPageServiceImpl implements StatsPageService {
    private final StatsRepository statsRepository;
    private final ModeStatsRepository modeStatsRepository;
    private final UserRepository userRepository;
    private final WeeklyStatsRepository weeklyStatsRepository;
    private final WeeklyUsersSummedRepository weeklyUsersSummedRepository;

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
        int numberOfAvgScoreGreaterThanZero = 0;
        for(Stats stat : stats) {
            totalGamePlayed += stat.getTotalGamePlayed();
            if(stat.getTotalGamePlayed() > 0) {
                avgScoreSummed = avgScoreSummed.add(stat.getAvgScore());
                numberOfAvgScoreGreaterThanZero++;
            }
            timePlayedSeconds += stat.getTimePlayed();
            totalScoredPoints += stat.getTotalScoredPoints();
            numberOfWonGames += stat.getNumberOfWonGames();
        }
        String timePlayed = TimeConverter.convertSecondsToTime(timePlayedSeconds);
        BigDecimal avgScore = avgScoreSummed.divide(BigDecimal.valueOf(numberOfAvgScoreGreaterThanZero), RoundingMode.DOWN);
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
            int numberOfAvgScoreGreaterThanZero = 0;
            for(ModeStats modeStat : modeStatsListOfCertainMode) {
                totalGamePlayed += modeStat.getTotalGamePlayed();
                if(modeStat.getTotalGamePlayed() > 0) {
                    avgScoreSummed = avgScoreSummed.add(modeStat.getAvgScore());
                    numberOfAvgScoreGreaterThanZero++;
                }
                timePlayedSeconds += modeStat.getTimePlayed();
                totalScoredPoints += modeStat.getTotalScoredPoints();
                numberOfWonGames += modeStat.getNumberOfWonGames();
            }
            String timePlayed = TimeConverter.convertSecondsToTime(timePlayedSeconds);
            BigDecimal divisor = BigDecimal.valueOf(numberOfAvgScoreGreaterThanZero);
            if(divisor.equals(BigDecimal.ZERO)) {
                divisor = BigDecimal.ONE;
            }
            BigDecimal avgScore = avgScoreSummed.divide(divisor, RoundingMode.DOWN);
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
        if(weeklyStats == null) {
            return new StatsWithUName();
        }
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
        return atomicReference.get();
    }
    @Override
    public void saveWeeklyStatsSnapshot() {
        LocalDate week = LocalDate.now();
        List<WeeklyStats> weeklyStats = new ArrayList<>();
        System.out.println("Started");
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
        System.out.println("Passed statsWithUNameOfAllUsersCurrent");

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

        System.out.println("Passed allGameStatsWithUNameOfAllUsersCurrent");

        StatsWithUName statsOfAllUsersCombinedCurrent = getStatsOfAllUsersCombinedCurrent();
        System.out.println("Hello");
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
        System.out.println("Size of allGameStatsWithUNameOfAllUsersCurrent: " + allGameStatsWithUNameOfAllUsersCurrent.size());

        Map<String, StatsWithUName> gameStatsOffAllUsersCombinedCurrent = getGameStatsOffAllUsersCombinedCurrent();
        System.out.println("Size of gameStatsOffAllUsersCombinedCurrent: " + gameStatsOffAllUsersCombinedCurrent.size());
        System.out.println("Total entries to save: " + weeklyStats.size());
        System.out.println(getGameStatsOffAllUsersCombinedCurrent().keySet().size());
        for(String mode : gameStatsOffAllUsersCombinedCurrent.keySet()) {
            System.out.println("Now " + mode + "!");
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
        System.out.println("Total entries to save2: " + weeklyStats.size());
        System.out.println("Passed gameStatsOffAllUsersCombinedCurrent");


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

    @Override
    public Map<String, StatsWithUName> getDifferenceGameStatsOffAllUsersCombined(String weekStr) {
        boolean isAllTime = weekStr.equals(ALL_TIME);
        Map<String, StatsWithUName> statsWithUNameMap = new HashMap<>();
        MindAndMaxWeek weekRange = getMindAndMaxWeek();
        LocalDate week = isAllTime ? weekRange.max : LocalDate.parse(weekStr);
        if (isAllTime) {
            Map<String, StatsWithUName> gameStatsOffAllUsersCombinedCurrent = getGameStatsOffAllUsersCombinedCurrent();
            Map<String, StatsWithUName> gameStatsOffAllUsersCombinedFromWeek = getGameStatsOffAllUsersCombinedFromWeek(week);
            if (!gameStatsOffAllUsersCombinedCurrent.keySet().containsAll(gameStatsOffAllUsersCombinedFromWeek.keySet())) {
                for (Mode mode : Mode.values()) {
                    statsWithUNameMap.put(mode.toString(), new StatsWithUName());
                }
            }
            for (String mode : gameStatsOffAllUsersCombinedCurrent.keySet()) {
                StatsWithUName statsWithUNameCurrent = gameStatsOffAllUsersCombinedCurrent.get(mode);
                StatsWithUName statsWithUNameBefore = gameStatsOffAllUsersCombinedFromWeek.get(mode);
                StatsWithUName statsDiff = getWithUNameWithUserIdAndName(statsWithUNameCurrent, statsWithUNameBefore);
                statsWithUNameMap.put(mode, statsDiff);
            }
            return statsWithUNameMap;
        } else {
            LocalDate prevWeek = week.minusDays(7);
            List<LocalDate> weeks = getWeeks();
            if ((prevWeek.isAfter(weekRange.min()) || prevWeek.equals(weekRange.min())) && weeks.contains(prevWeek)) {
                Map<String, StatsWithUName> gameStatsOffAllUsersCombinedCurrent = getGameStatsOffAllUsersCombinedCurrent();
                Map<String, StatsWithUName> gameStatsOffAllUsersCombinedFromWeek = getGameStatsOffAllUsersCombinedFromWeek(prevWeek);
                return getStringStatsWithUNameMap(gameStatsOffAllUsersCombinedFromWeek, gameStatsOffAllUsersCombinedCurrent, statsWithUNameMap);
            } else if(week.equals(weekRange.min)) {
                for (Mode mode : Mode.values()) {
                    statsWithUNameMap.put(mode.toString(), StatsWithUName.builder()
                                    .totalGamePlayed(0L)
                                    .totalScoredPoints(0L)
                                    .avgScore(BigDecimal.ZERO)
                                    .timePlayed(TimeConverter.convertSecondsToTime(0L))
                                    .numberOfWonGames(0)
                            .build());
                }
                return statsWithUNameMap;
            } else {
                Map<String, StatsWithUName> gameStatsOffAllUsersCombinedCurrent = getGameStatsOffAllUsersCombinedCurrent();
                Map<String, StatsWithUName> gameStatsOffAllUsersCombinedFromWeek = getGameStatsOffAllUsersCombinedFromWeek(weeks.stream().sorted(Comparator.reverseOrder()).toList().get(weeks.indexOf(week)+1));
                return getStringStatsWithUNameMap(gameStatsOffAllUsersCombinedFromWeek, gameStatsOffAllUsersCombinedCurrent, statsWithUNameMap);
            }
        }
    }

    private static Map<String, StatsWithUName> getStringStatsWithUNameMap(Map<String, StatsWithUName> gameStatsOffAllUsersCombinedFromWeek, Map<String, StatsWithUName> gameStatsOffAllUsersCombinedCurrent, Map<String, StatsWithUName> statsWithUNameMap) {
        if (gameStatsOffAllUsersCombinedFromWeek.get(Mode.HISTORY.toString()) == null || !gameStatsOffAllUsersCombinedCurrent.keySet().containsAll(gameStatsOffAllUsersCombinedFromWeek.keySet())) {
            for (Mode mode : Mode.values()) {
                statsWithUNameMap.put(mode.toString(), new StatsWithUName());
            }
            return statsWithUNameMap;
        }
        for (String mode : gameStatsOffAllUsersCombinedCurrent.keySet()) {
            StatsWithUName statsWithUNameCurrent = gameStatsOffAllUsersCombinedCurrent.get(mode);
            StatsWithUName statsWithUNameBefore = gameStatsOffAllUsersCombinedFromWeek.get(mode);
            StatsWithUName statsDiff = getWithUNameWithUserIdAndName(statsWithUNameCurrent, statsWithUNameBefore);
            statsWithUNameMap.put(mode, statsDiff);
        }
        return statsWithUNameMap;
    }

    private static StatsWithUName getWithUNameWithUserIdAndName(StatsWithUName statsWithUNameCurrent, StatsWithUName statsWithUNameBefore) {
        if(statsWithUNameBefore == null) {
            StatsWithUName blankStatsWithUName = getBlankStatsWithUName();
            blankStatsWithUName.setName(statsWithUNameCurrent.getName());
            blankStatsWithUName.setUserId(statsWithUNameCurrent.getUserId());
            return blankStatsWithUName;
        }
        return StatsWithUName.builder()
                .userId(statsWithUNameCurrent.getUserId())
                .totalGamePlayed(Math.max(statsWithUNameCurrent.getTotalGamePlayed()- statsWithUNameBefore.getTotalGamePlayed(), 0))
                .avgScore(BigDecimal.ZERO.max(statsWithUNameCurrent.getAvgScore().subtract(statsWithUNameBefore.getAvgScore())))
                .timePlayed(TimeConverter.differenceOfTime(statsWithUNameCurrent.getTimePlayed(), statsWithUNameBefore.getTimePlayed()))
                .totalScoredPoints(Math.max(statsWithUNameCurrent.getTotalScoredPoints()- statsWithUNameBefore.getTotalScoredPoints(),0))
                .numberOfWonGames(Math.max(statsWithUNameCurrent.getNumberOfWonGames()- statsWithUNameBefore.getNumberOfWonGames(),0))
                .name(statsWithUNameCurrent.getName())
                .build();
    }

    @Override
    public StatsWithUName getDifferenceStatsOfAllUsersCombined(String weekStr) {
        boolean isAllTime = weekStr.equals(ALL_TIME);
        LocalDate week = isAllTime ? LocalDate.now() : LocalDate.parse(weekStr);
        MindAndMaxWeek weekRange = getMindAndMaxWeek();
            if (isAllTime) {
                StatsWithUName statsWithUNameCurrent = getStatsOfAllUsersCombinedCurrent();
                StatsWithUName statsWithUNameFromWeek = getStatsOfAllUsersCombinedFromWeek(weekRange.max);
                return getStatsWithUName(statsWithUNameCurrent, statsWithUNameFromWeek);
            } else {
                List<LocalDate> weeks = getWeeks();
                LocalDate prevWeek = week.minusDays(7);
                if ((prevWeek.isAfter(weekRange.min()) || prevWeek.equals(weekRange.min())) && weeks.contains(prevWeek)) {
                    StatsWithUName statsWithUNameFromWeek = getStatsOfAllUsersCombinedFromWeek(week);
                    StatsWithUName statsWithUNameBefore = getStatsOfAllUsersCombinedFromWeek(prevWeek);
                    if (statsWithUNameBefore.getTotalScoredPoints() == null) {
                        return getBlankStatsWithUName();
                    }
                    return getStatsWithUName(statsWithUNameFromWeek, statsWithUNameBefore);
                } else if(week.equals(weekRange.min)) {
                    return getBlankStatsWithUName();
                } else {
                    StatsWithUName statsWithUNameFromWeek = getStatsOfAllUsersCombinedFromWeek(week);
                    StatsWithUName statsWithUNameBefore = getStatsOfAllUsersCombinedFromWeek(weeks.stream().sorted(Comparator.reverseOrder()).toList().get(weeks.indexOf(week)+1));
                    if (statsWithUNameBefore.getTotalScoredPoints() == null) {
                        return getBlankStatsWithUName();
                    }
                    return getStatsWithUName(statsWithUNameFromWeek, statsWithUNameBefore);
                }
            }
    }

    private static StatsWithUName getBlankStatsWithUName() {
        return StatsWithUName.builder()
                .totalGamePlayed(0L)
                .totalScoredPoints(0L)
                .avgScore(BigDecimal.ZERO)
                .timePlayed(TimeConverter.convertSecondsToTime(0L))
                .numberOfWonGames(0)
                .build();
    }

    @Override
    public UsersSummed getDifferenceUsersSummed(String weekStr) {
        boolean isAllTime = weekStr.equals(ALL_TIME);
        LocalDate week = isAllTime ? LocalDate.now() : LocalDate.parse(weekStr);
        MindAndMaxWeek weekRange = getMindAndMaxWeek();
        List<LocalDate> weeks = getWeeks();
        if (isAllTime) {
            UsersSummed usersSummedCurrent = getUsersSummedCurrent();
            UsersSummed usersSummedFromWeek = getUsersSummedFromWeek(weekRange.max);
            UsersSummed usersSummedDifference = new UsersSummed();
            usersSummedDifference.setGuestUsers(usersSummedCurrent.getGuestUsers() - usersSummedFromWeek.getGuestUsers());
            usersSummedDifference.setGoogleOrEmailUsers(usersSummedCurrent.getGoogleOrEmailUsers() - usersSummedFromWeek.getGoogleOrEmailUsers());
            return usersSummedDifference;
        } else if ((week.minusDays(7).isAfter(weekRange.min()) || week.minusDays(7).equals(weekRange.min())) && weeks.contains(week.minusDays(7))) {
            UsersSummed usersSummedFromWeek = getUsersSummedFromWeek(week);
            UsersSummed usersSummedBefore = getUsersSummedFromWeek(week.minusDays(7));
            if (usersSummedBefore == null) {
                return getBlankUsersSummed();
            }
            UsersSummed usersSummedDifference = new UsersSummed();
            usersSummedDifference.setGuestUsers(usersSummedFromWeek.getGuestUsers() - usersSummedBefore.getGuestUsers());
            usersSummedDifference.setGoogleOrEmailUsers(usersSummedFromWeek.getGoogleOrEmailUsers() - usersSummedBefore.getGoogleOrEmailUsers());
            return usersSummedDifference;
        } else if(week.equals(weekRange.min)){
            return getBlankUsersSummed();
        } else {
            UsersSummed usersSummedFromWeek = getUsersSummedFromWeek(week);
            UsersSummed usersSummedBefore = getUsersSummedFromWeek(weeks.stream().sorted(Comparator.reverseOrder()).toList().get((weeks.indexOf(week)+1)));
            if (usersSummedBefore == null) {
                return getBlankUsersSummed();
            }
            UsersSummed usersSummedDifference = new UsersSummed();
            usersSummedDifference.setGuestUsers(usersSummedFromWeek.getGuestUsers() - usersSummedBefore.getGuestUsers());
            usersSummedDifference.setGoogleOrEmailUsers(usersSummedFromWeek.getGoogleOrEmailUsers() - usersSummedBefore.getGoogleOrEmailUsers());
            return usersSummedDifference;
        }
    }

    private static UsersSummed getBlankUsersSummed() {
        UsersSummed usersSummed = new UsersSummed();
        usersSummed.setGoogleOrEmailUsers(0);
        usersSummed.setGuestUsers(0);
        return usersSummed;
    }

    private StatsWithUName getStatsWithUName(StatsWithUName statsWithUNameCurrent, StatsWithUName statsWithUNameBefore) {
        if(statsWithUNameCurrent == null || statsWithUNameBefore == null) {
            return getBlankStatsWithUName();
        }
        return StatsWithUName.builder()
                .totalGamePlayed(Math.max(statsWithUNameCurrent.getTotalGamePlayed()-statsWithUNameBefore.getTotalGamePlayed(), 0))
                .avgScore(statsWithUNameCurrent.getAvgScore().subtract(statsWithUNameBefore.getAvgScore()))
                .timePlayed(TimeConverter.differenceOfTime(statsWithUNameCurrent.getTimePlayed(), statsWithUNameBefore.getTimePlayed()))
                .totalScoredPoints(Math.max(statsWithUNameCurrent.getTotalScoredPoints()-statsWithUNameBefore.getTotalScoredPoints(),0))
                .numberOfWonGames(Math.max(statsWithUNameCurrent.getNumberOfWonGames()-statsWithUNameBefore.getNumberOfWonGames(),0))
                .build();
    }

    private MindAndMaxWeek getMindAndMaxWeek() {
        LocalDate min = LocalDate.MAX;
        LocalDate max = LocalDate.MIN;
        List<LocalDate> weeks = getWeeks();
        for (LocalDate localDate : weeks) {
            if (localDate.isBefore(min)) {
                min = localDate;
            }
            if (localDate.isAfter(max)) {
                max = localDate;
            }
        }
        return new MindAndMaxWeek(min, max);
    }
    private record MindAndMaxWeek(LocalDate min, LocalDate max) {}
}
