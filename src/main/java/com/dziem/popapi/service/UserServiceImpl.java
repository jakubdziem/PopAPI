package com.dziem.popapi.service;

import com.dziem.popapi.model.*;
import com.dziem.popapi.repository.LeaderboardRepository;
import com.dziem.popapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final StatsService statsService;
    private final ScoreService scoreService;
    private final UNameService uNameService;
    private final LeaderboardRepository leaderboardRepository;
    private final LeaderboardService leaderboardService;
    private final ModeStatsService modeStatsService;

    @Override
    public String generateUniqueUUID() {
        UUID uuid = UUID.randomUUID();
        while(userRepository.existsById(uuid.toString())) {
            uuid = UUID.randomUUID();
        }
        return uuid.toString();
    }
    @Override
    public User createUser(String userId, boolean guest) {
        User user = new User();
        user.setUserId(userId);
        user.setStatistics(statsService.initializeStats(user));
        List<Score> bestScore = new ArrayList<>();
        List<ModeStats> modeStatsList = new ArrayList<>();
        for(Mode mode : Mode.values()) {
            bestScore.add(scoreService.initializeScore(mode.toString(), user));
            modeStatsList.add(modeStatsService.initializeModeStats(user, mode.toString()));
        }
        user.setModeStats(modeStatsList);
        user.setBestScores(bestScore);
        user.setGuest(guest);
        user.setUName(UName.builder()
                .name(uNameService.generateRandomUserName())
                .lastUpdate(LocalDateTime.of(2000,1,1,0,0,0,0))
                .user(user)
                .build());
        return userRepository.save(user);
    }

    @Override
    public boolean userExists(String userId) {
        return userRepository.existsById(userId);
    }
    @Override
    public Optional<String> migrateProfileToGoogle(String anonimUserId, String googleId) {
        if (userExists(anonimUserId)) {
            User anonimUser = userRepository.findById(anonimUserId).get();
            User googleUser = User.builder()
            .userId(googleId)
                    .build();
            googleUser.setStatistics(Stats.builder()
                    .user(googleUser)
                    .avgScore(anonimUser.getStatistics().getAvgScore())
                    .numberOfWonGames(anonimUser.getStatistics().getNumberOfWonGames())
                    .timePlayed(anonimUser.getStatistics().getTimePlayed())
                    .totalGamePlayed(anonimUser.getStatistics().getTotalGamePlayed())
                    .totalScoredPoints(anonimUser.getStatistics().getTotalScoredPoints())
                    .build());
            List<Score> googleBestScores = new ArrayList<>();
            googleUser.setGuest(false);
            googleUser = userRepository.save(googleUser); //persist in db to successfully save leaderboard

            googleUser.setUName(UName.builder()
                    .name(anonimUser.getUName().getName())
                    .lastUpdate(LocalDateTime.of(2000,1, 1, 0, 0, 0))
                    .user(googleUser)
                    .build());
            List<Leaderboard> leaderboardList = new ArrayList<>();
            for(Score score : anonimUser.getBestScores()) {

                Leaderboard leaderboardEntity = Leaderboard.builder()
                        .score(score.getBestScore())
                            .mode(score.getMode())
                            .user(googleUser)
                            .name(googleUser.getUName().getName())
                            .build();
                leaderboardList.add(leaderboardEntity);
                leaderboardRepository.save(leaderboardEntity);

                score.setUser(googleUser);
                googleBestScores.add(score);
            }

            List<ModeStats> modeStatsList = new ArrayList<>();
            for(ModeStats modeStats : anonimUser.getModeStats()) {
                modeStats.setUser(googleUser);
                modeStatsList.add(modeStats);
            }

            googleUser.setModeStats(modeStatsList);
            googleUser.setBestScores(googleBestScores);
            googleUser.setLeaderboard(leaderboardList);
            userRepository.delete(anonimUser);
            userRepository.save(googleUser);
            return Optional.of(googleUser.getUName().getName());
        }
        return Optional.empty();


    }

    @Override
    public Optional<String> createGoogleUser(String googleId) {
        if(userRepository.existsById(googleId)) {
            return Optional.empty();
        } else {
            User user = createUser(googleId, false);
            user.setLeaderboard(leaderboardService.initializeLeaderboard(googleId, user));
            userRepository.save(user);
            return Optional.of(user.getUName().getName());
        }
    }
}
