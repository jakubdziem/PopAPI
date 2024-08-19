package com.dziem.popapi.service;

import com.dziem.popapi.model.Mode;
import com.dziem.popapi.model.Score;
import com.dziem.popapi.model.User;
import com.dziem.popapi.repository.ModeRepository;
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
    private final ModeRepository modeRepository;
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
        for(Mode mode : modeRepository.findAll()) {
            bestScore.add(scoreService.initializeScore(mode, user));
        }
        user.setBestScores(bestScore);
        return userRepository.save(user);
    }
}
