package com.dziem.popapi.service;

import com.dziem.popapi.dto.webpage.StatsWithUNameDTO;
import com.dziem.popapi.dto.webpage.UsersSummedDTO;
import com.dziem.popapi.formatter.TimeConverter;
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
    public List<StatsWithUNameDTO> getStatsWithUNameOfAllUsersCurrent() {
        return getAllStatsWithUname(COMBINED_STATS);
    }

    @Override
    public Map<String, List<StatsWithUNameDTO>> getAllGameStatsWithUNameOfAllUsersCurrent() {
        Map<String, List<StatsWithUNameDTO>> modeStatsWithUNameHashMap = new HashMap<>();
        for(Mode mode : Mode.values()) {
            List<StatsWithUNameDTO> modeStatsWithUNameDTOByCertainMode = getAllStatsWithUname(mode.toString());
            modeStatsWithUNameHashMap.put(mode.toString(), modeStatsWithUNameDTOByCertainMode);
        }
        return modeStatsWithUNameHashMap;
    }

    @Override
    public StatsWithUNameDTO getStatsOfAllUsersCombinedCurrent() {
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
        return StatsWithUNameDTO.builder()
                .totalGamePlayed(totalGamePlayed)
                .avgScore(avgScore)
                .timePlayed(timePlayed)
                .totalScoredPoints(totalScoredPoints)
                .numberOfWonGames(numberOfWonGames)
                .build();
    }

    @Override
    public Map<String, StatsWithUNameDTO> getGameStatsOffAllUsersCombinedCurrent() {
        Map<String, StatsWithUNameDTO> modeStatsWithUNameHashMap = new HashMap<>();
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
            StatsWithUNameDTO singleModeStat = StatsWithUNameDTO.builder()
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
    public UsersSummedDTO getUsersSummedCurrent() {
        Map<Boolean, List<User>> usersDistiguishedPerGuestBoolean = userRepository.findAll().stream().collect(groupingBy(User::isGuest));
        List<User> guests = usersDistiguishedPerGuestBoolean.get(true);
        List<User> googleOrEmailUsers = usersDistiguishedPerGuestBoolean.get(false);
        UsersSummedDTO usersSummedDTO = new UsersSummedDTO();
        usersSummedDTO.setGuestUsers(guests.size());
        usersSummedDTO.setGoogleOrEmailUsers(googleOrEmailUsers.size());
        return usersSummedDTO;
    }

    private List<StatsWithUNameDTO> getAllStatsWithUname(String mode) {
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
        List<StatsWithUNameDTO> statsWithUNameDTOList = new ArrayList<>();
        for (Object[] row : result) {
            StatsWithUNameDTO statsWithUNameDTO = StatsWithUNameDTO.builder()
                    .userId((String) row[0])
                    .totalGamePlayed((Long) row[1])
                    .avgScore(new BigDecimal(String.valueOf(row[2])))
                    .timePlayed(TimeConverter.convertSecondsToTime((Long) row[3]))
                    .totalScoredPoints((Long) row[4])
                    .numberOfWonGames((Integer) row[5])
                    .name((String) row[6])
                    .build();
            statsWithUNameDTOList.add(statsWithUNameDTO);
        }
        return statsWithUNameDTOList;
    }

    @Override
    public List<StatsWithUNameDTO> getStatsWithUNameOfAllUsersFromWeek(LocalDate week) {
        List<WeeklyStats> weeklyStats = weeklyStatsRepository.getStatsWithUNameOfAllUsersFromWeek(week);
        List<StatsWithUNameDTO> statsWithUNameDTOS = new ArrayList<>();
        for(WeeklyStats stat : weeklyStats) {
            statsWithUNameDTOS.add(StatsWithUNameDTO.builder()
                    .userId(stat.getUserId())
                    .totalGamePlayed(stat.getTotalGamePlayed())
                    .avgScore(stat.getAvgScore())
                    .timePlayed(stat.getTimePlayed())
                    .totalScoredPoints(stat.getTotalScoredPoints())
                    .numberOfWonGames(stat.getNumberOfWonGames())
                    .name(stat.getName())
                    .build());
        }
        return statsWithUNameDTOS;
    }

    @Override
    public Map<String, List<StatsWithUNameDTO>> getAllGameStatsWithUNameOfAllUsersFromWeek(LocalDate week) {
        List<WeeklyStats> weeklyStats = weeklyStatsRepository.getAllGameStatsWithUNameOfAllUsersFromWeek(week);
        Map<String, List<StatsWithUNameDTO>> gameStatsWithUnameMap = new HashMap<>();
        for(Mode mode : Mode.values()) {
            List<StatsWithUNameDTO> gameStatsWithUnameDTOS = new ArrayList<>();
            List<WeeklyStats> filteredWeeklyStatsFromLoopedMode = weeklyStats.stream().filter(weeklyStat -> weeklyStat.getMode().equals(mode.toString())).toList();
            for(WeeklyStats weeklyStat : filteredWeeklyStatsFromLoopedMode) {
                gameStatsWithUnameDTOS.add(StatsWithUNameDTO.builder()
                        .userId(weeklyStat.getUserId())
                        .totalGamePlayed(weeklyStat.getTotalGamePlayed())
                        .avgScore(weeklyStat.getAvgScore())
                        .timePlayed(weeklyStat.getTimePlayed())
                        .totalScoredPoints(weeklyStat.getTotalScoredPoints())
                        .numberOfWonGames(weeklyStat.getNumberOfWonGames())
                        .name(weeklyStat.getName())
                        .build());
            }
            gameStatsWithUnameMap.put(mode.toString(), gameStatsWithUnameDTOS);
        }
        return gameStatsWithUnameMap;
    }

    @Override
    public StatsWithUNameDTO getStatsOfAllUsersCombinedFromWeek(LocalDate week) {
        WeeklyStats weeklyStats = weeklyStatsRepository.getStatsOfAllUsersCombinedFromWeek(week);
        if(weeklyStats == null) {
            return new StatsWithUNameDTO();
        }
        return StatsWithUNameDTO.builder()
                .totalGamePlayed(weeklyStats.getTotalGamePlayed())
                .avgScore(weeklyStats.getAvgScore())
                .timePlayed(weeklyStats.getTimePlayed())
                .totalScoredPoints(weeklyStats.getTotalScoredPoints())
                .numberOfWonGames(weeklyStats.getNumberOfWonGames())
                .build();
    }

    @Override
    public Map<String, StatsWithUNameDTO> getGameStatsOffAllUsersCombinedFromWeek(LocalDate week) {
        List<WeeklyStats> allGameStatsWithUNameOfAllUsersFromWeek = weeklyStatsRepository.getGameStatsOffAllUsersCombinedFromWeek(week);
        Map<String, StatsWithUNameDTO> statsWithUNameMap = new HashMap<>();
        for(WeeklyStats weeklyStats : allGameStatsWithUNameOfAllUsersFromWeek) {
            statsWithUNameMap.put(weeklyStats.getMode(), StatsWithUNameDTO.builder()
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
    public UsersSummedDTO getUsersSummedFromWeek(LocalDate week) {
        AtomicReference<UsersSummedDTO> atomicReference = new AtomicReference<>();
        weeklyUsersSummedRepository.findById(week)
                .ifPresentOrElse(
                        weeklyUsersSummed -> {
                            UsersSummedDTO usersSummedDTO = new UsersSummedDTO();
                            usersSummedDTO.setGuestUsers(weeklyUsersSummed.getGuestUsers());
                            usersSummedDTO.setGoogleOrEmailUsers(weeklyUsersSummed.getGoogleOrEmailUsers());
                            atomicReference.set(usersSummedDTO);
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
        List<StatsWithUNameDTO> statsWithUNameDTOOfAllUsersCurrent = getStatsWithUNameOfAllUsersCurrent();
        for(StatsWithUNameDTO stat : statsWithUNameDTOOfAllUsersCurrent) {
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

        Map<String, List<StatsWithUNameDTO>> allGameStatsWithUNameOfAllUsersCurrent = getAllGameStatsWithUNameOfAllUsersCurrent();
        for(String mode : allGameStatsWithUNameOfAllUsersCurrent.keySet()) {
            for(StatsWithUNameDTO stat : allGameStatsWithUNameOfAllUsersCurrent.get(mode)) {
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

        StatsWithUNameDTO statsOfAllUsersCombinedCurrent = getStatsOfAllUsersCombinedCurrent();
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

        Map<String, StatsWithUNameDTO> gameStatsOffAllUsersCombinedCurrent = getGameStatsOffAllUsersCombinedCurrent();
        System.out.println("Size of gameStatsOffAllUsersCombinedCurrent: " + gameStatsOffAllUsersCombinedCurrent.size());
        System.out.println("Total entries to save: " + weeklyStats.size());
        System.out.println(getGameStatsOffAllUsersCombinedCurrent().keySet().size());
        for(String mode : gameStatsOffAllUsersCombinedCurrent.keySet()) {
            System.out.println("Now " + mode + "!");
            StatsWithUNameDTO stat = gameStatsOffAllUsersCombinedCurrent.get(mode);
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

        UsersSummedDTO usersSummedDTOCurrent = getUsersSummedCurrent();
        WeeklyUsersSummed weeklyUsersSummed = WeeklyUsersSummed.builder()
                .weekStartDate(week)
                .guestUsers(usersSummedDTOCurrent.getGuestUsers())
                .googleOrEmailUsers(usersSummedDTOCurrent.getGoogleOrEmailUsers())
                .build();
        weeklyUsersSummedRepository.save(weeklyUsersSummed);
    }

    @Override
    public List<LocalDate> getWeeks() {
        return weeklyStatsRepository.getWeeks();
    }

    @Override
    public Map<String, StatsWithUNameDTO> getDifferenceGameStatsOffAllUsersCombined(String weekStr) {
        boolean isAllTime = weekStr.equals(ALL_TIME);
        Map<String, StatsWithUNameDTO> statsWithUNameMap = new HashMap<>();
        MindAndMaxWeek weekRange = getMindAndMaxWeek();
        LocalDate week = isAllTime ? weekRange.max : LocalDate.parse(weekStr);
        if (isAllTime) {
            Map<String, StatsWithUNameDTO> gameStatsOffAllUsersCombinedCurrent = getGameStatsOffAllUsersCombinedCurrent();
            Map<String, StatsWithUNameDTO> gameStatsOffAllUsersCombinedFromWeek = getGameStatsOffAllUsersCombinedFromWeek(week);
            if (!gameStatsOffAllUsersCombinedCurrent.keySet().containsAll(gameStatsOffAllUsersCombinedFromWeek.keySet())) {
                for (Mode mode : Mode.values()) {
                    statsWithUNameMap.put(mode.toString(), new StatsWithUNameDTO());
                }
            }
            for (String mode : gameStatsOffAllUsersCombinedCurrent.keySet()) {
                StatsWithUNameDTO statsWithUNameDTOCurrent = gameStatsOffAllUsersCombinedCurrent.get(mode);
                StatsWithUNameDTO statsWithUNameDTOBefore = gameStatsOffAllUsersCombinedFromWeek.get(mode);
                StatsWithUNameDTO statsDiff = getWithUNameWithUserIdAndName(statsWithUNameDTOCurrent, statsWithUNameDTOBefore);
                statsWithUNameMap.put(mode, statsDiff);
            }
            return statsWithUNameMap;
        } else {
            LocalDate prevWeek = week.minusDays(7);
            List<LocalDate> weeks = getWeeks();
            if ((prevWeek.isAfter(weekRange.min()) || prevWeek.equals(weekRange.min())) && weeks.contains(prevWeek)) {
                Map<String, StatsWithUNameDTO> gameStatsOffAllUsersCombinedCurrent = getGameStatsOffAllUsersCombinedCurrent();
                Map<String, StatsWithUNameDTO> gameStatsOffAllUsersCombinedFromWeek = getGameStatsOffAllUsersCombinedFromWeek(prevWeek);
                return getStringStatsWithUNameMap(gameStatsOffAllUsersCombinedFromWeek, gameStatsOffAllUsersCombinedCurrent, statsWithUNameMap);
            } else if(week.equals(weekRange.min)) {
                for (Mode mode : Mode.values()) {
                    statsWithUNameMap.put(mode.toString(), StatsWithUNameDTO.builder()
                                    .totalGamePlayed(0L)
                                    .totalScoredPoints(0L)
                                    .avgScore(BigDecimal.ZERO)
                                    .timePlayed(TimeConverter.convertSecondsToTime(0L))
                                    .numberOfWonGames(0)
                            .build());
                }
                return statsWithUNameMap;
            } else {
                Map<String, StatsWithUNameDTO> gameStatsOffAllUsersCombinedCurrent = getGameStatsOffAllUsersCombinedCurrent();
                Map<String, StatsWithUNameDTO> gameStatsOffAllUsersCombinedFromWeek = getGameStatsOffAllUsersCombinedFromWeek(weeks.stream().sorted(Comparator.reverseOrder()).toList().get(weeks.indexOf(week)+1));
                return getStringStatsWithUNameMap(gameStatsOffAllUsersCombinedFromWeek, gameStatsOffAllUsersCombinedCurrent, statsWithUNameMap);
            }
        }
    }

    private static Map<String, StatsWithUNameDTO> getStringStatsWithUNameMap(Map<String, StatsWithUNameDTO> gameStatsOffAllUsersCombinedFromWeek, Map<String, StatsWithUNameDTO> gameStatsOffAllUsersCombinedCurrent, Map<String, StatsWithUNameDTO> statsWithUNameMap) {
        if (gameStatsOffAllUsersCombinedFromWeek.get(Mode.HISTORY.toString()) == null || !gameStatsOffAllUsersCombinedCurrent.keySet().containsAll(gameStatsOffAllUsersCombinedFromWeek.keySet())) {
            for (Mode mode : Mode.values()) {
                statsWithUNameMap.put(mode.toString(), new StatsWithUNameDTO());
            }
            return statsWithUNameMap;
        }
        for (String mode : gameStatsOffAllUsersCombinedCurrent.keySet()) {
            StatsWithUNameDTO statsWithUNameDTOCurrent = gameStatsOffAllUsersCombinedCurrent.get(mode);
            StatsWithUNameDTO statsWithUNameDTOBefore = gameStatsOffAllUsersCombinedFromWeek.get(mode);
            StatsWithUNameDTO statsDiff = getWithUNameWithUserIdAndName(statsWithUNameDTOCurrent, statsWithUNameDTOBefore);
            statsWithUNameMap.put(mode, statsDiff);
        }
        return statsWithUNameMap;
    }

    private static StatsWithUNameDTO getWithUNameWithUserIdAndName(StatsWithUNameDTO statsWithUNameDTOCurrent, StatsWithUNameDTO statsWithUNameDTOBefore) {
        if(statsWithUNameDTOBefore == null) {
            StatsWithUNameDTO blankStatsWithUNameDTO = getBlankStatsWithUName();
            blankStatsWithUNameDTO.setName(statsWithUNameDTOCurrent.getName());
            blankStatsWithUNameDTO.setUserId(statsWithUNameDTOCurrent.getUserId());
            return blankStatsWithUNameDTO;
        }
        return StatsWithUNameDTO.builder()
                .userId(statsWithUNameDTOCurrent.getUserId())
                .totalGamePlayed(Math.max(statsWithUNameDTOCurrent.getTotalGamePlayed()- statsWithUNameDTOBefore.getTotalGamePlayed(), 0))
                .avgScore(BigDecimal.ZERO.max(statsWithUNameDTOCurrent.getAvgScore().subtract(statsWithUNameDTOBefore.getAvgScore())))
                .timePlayed(TimeConverter.differenceOfTime(statsWithUNameDTOCurrent.getTimePlayed(), statsWithUNameDTOBefore.getTimePlayed()))
                .totalScoredPoints(Math.max(statsWithUNameDTOCurrent.getTotalScoredPoints()- statsWithUNameDTOBefore.getTotalScoredPoints(),0))
                .numberOfWonGames(Math.max(statsWithUNameDTOCurrent.getNumberOfWonGames()- statsWithUNameDTOBefore.getNumberOfWonGames(),0))
                .name(statsWithUNameDTOCurrent.getName())
                .build();
    }

    @Override
    public StatsWithUNameDTO getDifferenceStatsOfAllUsersCombined(String weekStr) {
        boolean isAllTime = weekStr.equals(ALL_TIME);
        LocalDate week = isAllTime ? LocalDate.now() : LocalDate.parse(weekStr);
        MindAndMaxWeek weekRange = getMindAndMaxWeek();
            if (isAllTime) {
                StatsWithUNameDTO statsWithUNameDTOCurrent = getStatsOfAllUsersCombinedCurrent();
                StatsWithUNameDTO statsWithUNameDTOFromWeek = getStatsOfAllUsersCombinedFromWeek(weekRange.max);
                return getStatsWithUName(statsWithUNameDTOCurrent, statsWithUNameDTOFromWeek);
            } else {
                List<LocalDate> weeks = getWeeks();
                LocalDate prevWeek = week.minusDays(7);
                if ((prevWeek.isAfter(weekRange.min()) || prevWeek.equals(weekRange.min())) && weeks.contains(prevWeek)) {
                    StatsWithUNameDTO statsWithUNameDTOFromWeek = getStatsOfAllUsersCombinedFromWeek(week);
                    StatsWithUNameDTO statsWithUNameDTOBefore = getStatsOfAllUsersCombinedFromWeek(prevWeek);
                    if (statsWithUNameDTOBefore.getTotalScoredPoints() == null) {
                        return getBlankStatsWithUName();
                    }
                    return getStatsWithUName(statsWithUNameDTOFromWeek, statsWithUNameDTOBefore);
                } else if(week.equals(weekRange.min)) {
                    return getBlankStatsWithUName();
                } else {
                    StatsWithUNameDTO statsWithUNameDTOFromWeek = getStatsOfAllUsersCombinedFromWeek(week);
                    StatsWithUNameDTO statsWithUNameDTOBefore = getStatsOfAllUsersCombinedFromWeek(weeks.stream().sorted(Comparator.reverseOrder()).toList().get(weeks.indexOf(week)+1));
                    if (statsWithUNameDTOBefore.getTotalScoredPoints() == null) {
                        return getBlankStatsWithUName();
                    }
                    return getStatsWithUName(statsWithUNameDTOFromWeek, statsWithUNameDTOBefore);
                }
            }
    }

    private static StatsWithUNameDTO getBlankStatsWithUName() {
        return StatsWithUNameDTO.builder()
                .totalGamePlayed(0L)
                .totalScoredPoints(0L)
                .avgScore(BigDecimal.ZERO)
                .timePlayed(TimeConverter.convertSecondsToTime(0L))
                .numberOfWonGames(0)
                .build();
    }

    @Override
    public UsersSummedDTO getDifferenceUsersSummed(String weekStr) {
        boolean isAllTime = weekStr.equals(ALL_TIME);
        LocalDate week = isAllTime ? LocalDate.now() : LocalDate.parse(weekStr);
        MindAndMaxWeek weekRange = getMindAndMaxWeek();
        List<LocalDate> weeks = getWeeks();
        if (isAllTime) {
            UsersSummedDTO usersSummedDTOCurrent = getUsersSummedCurrent();
            UsersSummedDTO usersSummedDTOFromWeek = getUsersSummedFromWeek(weekRange.max);
            UsersSummedDTO usersSummedDTODifference = new UsersSummedDTO();
            usersSummedDTODifference.setGuestUsers(usersSummedDTOCurrent.getGuestUsers() - usersSummedDTOFromWeek.getGuestUsers());
            usersSummedDTODifference.setGoogleOrEmailUsers(usersSummedDTOCurrent.getGoogleOrEmailUsers() - usersSummedDTOFromWeek.getGoogleOrEmailUsers());
            return usersSummedDTODifference;
        } else if ((week.minusDays(7).isAfter(weekRange.min()) || week.minusDays(7).equals(weekRange.min())) && weeks.contains(week.minusDays(7))) {
            UsersSummedDTO usersSummedDTOFromWeek = getUsersSummedFromWeek(week);
            UsersSummedDTO usersSummedDTOBefore = getUsersSummedFromWeek(week.minusDays(7));
            if (usersSummedDTOBefore == null) {
                return getBlankUsersSummed();
            }
            UsersSummedDTO usersSummedDTODifference = new UsersSummedDTO();
            usersSummedDTODifference.setGuestUsers(usersSummedDTOFromWeek.getGuestUsers() - usersSummedDTOBefore.getGuestUsers());
            usersSummedDTODifference.setGoogleOrEmailUsers(usersSummedDTOFromWeek.getGoogleOrEmailUsers() - usersSummedDTOBefore.getGoogleOrEmailUsers());
            return usersSummedDTODifference;
        } else if(week.equals(weekRange.min)){
            return getBlankUsersSummed();
        } else {
            UsersSummedDTO usersSummedDTOFromWeek = getUsersSummedFromWeek(week);
            UsersSummedDTO usersSummedDTOBefore = getUsersSummedFromWeek(weeks.stream().sorted(Comparator.reverseOrder()).toList().get((weeks.indexOf(week)+1)));
            if (usersSummedDTOBefore == null) {
                return getBlankUsersSummed();
            }
            UsersSummedDTO usersSummedDTODifference = new UsersSummedDTO();
            usersSummedDTODifference.setGuestUsers(usersSummedDTOFromWeek.getGuestUsers() - usersSummedDTOBefore.getGuestUsers());
            usersSummedDTODifference.setGoogleOrEmailUsers(usersSummedDTOFromWeek.getGoogleOrEmailUsers() - usersSummedDTOBefore.getGoogleOrEmailUsers());
            return usersSummedDTODifference;
        }
    }

    private static UsersSummedDTO getBlankUsersSummed() {
        UsersSummedDTO usersSummedDTO = new UsersSummedDTO();
        usersSummedDTO.setGoogleOrEmailUsers(0);
        usersSummedDTO.setGuestUsers(0);
        return usersSummedDTO;
    }

    private StatsWithUNameDTO getStatsWithUName(StatsWithUNameDTO statsWithUNameDTOCurrent, StatsWithUNameDTO statsWithUNameDTOBefore) {
        if(statsWithUNameDTOCurrent == null || statsWithUNameDTOBefore == null
                || statsWithUNameDTOBefore.getTotalGamePlayed() == null || statsWithUNameDTOCurrent.getTotalGamePlayed() == null) {
            return getBlankStatsWithUName();
        }
        return StatsWithUNameDTO.builder()
                .totalGamePlayed(Math.max(statsWithUNameDTOCurrent.getTotalGamePlayed()- statsWithUNameDTOBefore.getTotalGamePlayed(), 0))
                .avgScore(statsWithUNameDTOCurrent.getAvgScore().subtract(statsWithUNameDTOBefore.getAvgScore()))
                .timePlayed(TimeConverter.differenceOfTime(statsWithUNameDTOCurrent.getTimePlayed(), statsWithUNameDTOBefore.getTimePlayed()))
                .totalScoredPoints(Math.max(statsWithUNameDTOCurrent.getTotalScoredPoints()- statsWithUNameDTOBefore.getTotalScoredPoints(),0))
                .numberOfWonGames(Math.max(statsWithUNameDTOCurrent.getNumberOfWonGames()- statsWithUNameDTOBefore.getNumberOfWonGames(),0))
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
