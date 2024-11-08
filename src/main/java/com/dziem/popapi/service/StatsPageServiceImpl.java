package com.dziem.popapi.service;

import com.dziem.popapi.model.Mode;
import com.dziem.popapi.model.ModeStats;
import com.dziem.popapi.model.Stats;
import com.dziem.popapi.model.User;
import com.dziem.popapi.model.webpage.StatsWithUName;
import com.dziem.popapi.model.webpage.TimeConverter;
import com.dziem.popapi.model.webpage.UsersSummed;
import com.dziem.popapi.repository.ModeStatsRepository;
import com.dziem.popapi.repository.StatsRepository;
import com.dziem.popapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
public class StatsPageServiceImpl implements StatsPageService {
    private final StatsRepository statsRepository;
    private final ModeStatsRepository modeStatsRepository;
    private final UserRepository userRepository;
    private final String COMBINED_STATS = "COMBINEDSTATS";
    @Override
    public List<StatsWithUName> getStatsWithUNameOfAllUsers() {
        return getAllStatsWithUname(COMBINED_STATS);
    }

    @Override
    public Map<String, List<StatsWithUName>> getAllGameStatsWithUNameOfAllUsers() {
        Map<String, List<StatsWithUName>> modeStatsWithUNameHashMap = new HashMap<>();
        for(Mode mode : Mode.values()) {
            List<StatsWithUName> modeStatsWithUNameByCertainMode = getAllStatsWithUname(mode.toString());
            modeStatsWithUNameHashMap.put(mode.toString(), modeStatsWithUNameByCertainMode);
        }
        return modeStatsWithUNameHashMap;
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
    public Map<String, StatsWithUName> getGameStatsOffAllUsersCombined() {
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
    public UsersSummed getUsersSummed() {
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
}
