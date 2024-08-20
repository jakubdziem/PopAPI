package com.dziem.popapi.service;

import com.dziem.popapi.mapper.ScoreMapper;
import com.dziem.popapi.model.Score;
import com.dziem.popapi.model.ScoreDTO;
import com.dziem.popapi.model.User;
import com.dziem.popapi.repository.ScoreRepository;
import com.dziem.popapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
@Service
@RequiredArgsConstructor
public class ScoreServiceImpl implements ScoreService {
    private final ScoreRepository scoreRepository;
    private final UserRepository userRepository;
    private final ScoreMapper scoreMapper;
    @Override
    public Score initializeScore(String mode, User user) {
        Score score = new Score();
        score.setBestScore(0);
        score.setMode(mode);
        score.setUser(user);
        return scoreRepository.save(score);
    }

    @Override
    public boolean updateBestScore(UUID uuid, String mode, String newScore) {
        AtomicBoolean result = new AtomicBoolean(false);
        userRepository.findById(uuid).ifPresentOrElse(existing -> {
            result.set(true);
            List<Score> bestScores = existing.getBestScores();
            List<Score> scoreToUpdate = bestScores.stream().filter(score -> score.getMode().equals(mode)).toList();
            Score score = scoreToUpdate.get(0);
            score.setBestScore(Integer.parseInt(newScore));
            scoreRepository.save(score);
            }, () -> result.set(false));
        return result.get();

    }

    @Override
    public ScoreDTO getScoreByIdAndMode(UUID anonimUserId, String mode) {
        List<Score> bestScores = userRepository.findById(anonimUserId).get().getBestScores();
        for(int i = 0; i < bestScores.size(); i++) {
            String currentMode = bestScores.get(i).getMode();
            if(currentMode.equals(mode))
                return scoreMapper.scoreToScoreDTO(bestScores.get(i));
        }
        return new ScoreDTO();
    }
}
