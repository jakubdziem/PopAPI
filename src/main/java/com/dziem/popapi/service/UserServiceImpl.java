package com.dziem.popapi.service;

import com.dziem.popapi.model.Mode;
import com.dziem.popapi.model.Score;
import com.dziem.popapi.model.Stats;
import com.dziem.popapi.model.User;
import com.dziem.popapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final StatsService statsService;
    private final ScoreService scoreService;

    @Override
    public UUID generateUniqueUUID() {
        UUID uuid = UUID.randomUUID();
        while(userRepository.existsById(uuid)) {
            uuid = UUID.randomUUID();
        }
        return uuid;
    }

    @Override
    public User createAnonimUser(UUID uuid) {
        User user = new User();
        user.setUserId(uuid);
        user.setStatistics(statsService.initializeStats(user));
        List<Score> bestScore = new ArrayList<>();

        for(Mode mode : Mode.values()) {
            bestScore.add(scoreService.initializeScore(mode.toString(), user));
        }
        user.setBestScores(bestScore);
        return userRepository.save(user);
    }

    @Override
    public boolean userExists(UUID anonimUserId) {
        return userRepository.existsById(anonimUserId);
    }

    @Override
    public boolean migrateProfileToGoogle(UUID anonimUserId, String googleId) {
        if (userExists(anonimUserId)) {
            User anonimUser = userRepository.findById(anonimUserId).get();
            userRepository.delete(anonimUser);
            User googleUser = User.builder()
            .userId(UUID.fromString(googleId))
                    .build();
            Stats stats = Stats.builder()
                    .user(googleUser)
                    .avgScore(anonimUser.getStatistics().getAvgScore())
                    .numberOfWonGames(anonimUser.getStatistics().getNumberOfWonGames())
                    .timePlayed(anonimUser.getStatistics().getTimePlayed())
                    .totalGamePlayed(anonimUser.getStatistics().getTotalGamePlayed())
                    .totalScoredPoints(anonimUser.getStatistics().getTotalScoredPoints())
                    .build();
            googleUser.setStatistics(stats);
            List<Score> googleBestScores = new ArrayList<>();
            for(Score score : anonimUser.getBestScores()) {
                score.setUser(googleUser);
                googleBestScores.add(score);
            }
            googleUser.setBestScores(googleBestScores);
            userRepository.save(googleUser);
            return true;
        }
        return false;


    }
}
