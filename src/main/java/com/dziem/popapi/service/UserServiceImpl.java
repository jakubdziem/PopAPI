package com.dziem.popapi.service;

import com.dziem.popapi.model.*;
import com.dziem.popapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final StatsService statsService;
    private final ScoreService scoreService;
    private final UNameService uNameService;

    @Override
    public String generateUniqueUUID() {
        UUID uuid = UUID.randomUUID();
        while(userRepository.existsById(uuid.toString())) {
            uuid = UUID.randomUUID();
        }
        return uuid.toString();
    }

    @Override
    public User createAnonimUser(String userId) {
        User user = new User();
        user.setUserId(userId);
        user.setStatistics(statsService.initializeStats(user));
        List<Score> bestScore = new ArrayList<>();

        for(Mode mode : Mode.values()) {
            bestScore.add(scoreService.initializeScore(mode.toString(), user));
        }
        user.setBestScores(bestScore);
//        user.setUserName(userNameService.initializeUserName(user));
        return userRepository.save(user);
    }

    @Override
    public boolean userExists(String anonimUserId) {
        return userRepository.existsById(anonimUserId);
    }

    @Override
    public boolean migrateProfileToGoogle(String anonimUserId, String googleId) {
        if (userExists(anonimUserId) || userExists(googleId)) {
            User anonimUser = userRepository.findById(anonimUserId).get();
            userRepository.delete(anonimUser);
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
            for(Score score : anonimUser.getBestScores()) {
                score.setUser(googleUser);
                googleBestScores.add(score);
            }
            googleUser.setBestScores(googleBestScores);
            googleUser.setUName(UName.builder()
                            .name(uNameService.generateRandomUserName())
                            .lastUpdate(LocalDateTime.now())
                            .user(googleUser)
                    .build());
            userRepository.save(googleUser);
            return true;
        }
        return false;


    }
}
