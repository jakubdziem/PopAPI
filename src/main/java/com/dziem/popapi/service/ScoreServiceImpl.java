package com.dziem.popapi.service;

import com.dziem.popapi.mapper.ScoreMapper;
import com.dziem.popapi.model.Mode;
import com.dziem.popapi.model.Score;
import com.dziem.popapi.model.ScoreDTO;
import com.dziem.popapi.model.User;
import com.dziem.popapi.repository.ScoreRepository;
import com.dziem.popapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public ScoreDTO getScoreByUserIdAndMode(UUID anonimUserId, String mode) {
        List<Score> bestScores = userRepository.findById(anonimUserId).get().getBestScores();
        for (Score bestScore : bestScores) {
            String currentMode = bestScore.getMode();
            if (currentMode.equals(mode))
                return scoreMapper.scoreToScoreDTO(bestScore);
        }
        return new ScoreDTO();
    }

    @Override
    public List<ScoreDTO> getScoreById(UUID anonimUserId) {
        List<Score> bestScores = userRepository.findById(anonimUserId).get().getBestScores();
        List<ScoreDTO> scoreDTOS = new ArrayList<>();
        for(Score score : bestScores) {
            scoreDTOS.add(scoreMapper.scoreToScoreDTO(score));
        }
        return scoreDTOS;
    }

    @Override
    public boolean checkIsMode(String mode) {
        for(Mode modeEnum : Mode.values()) {
            if(mode.equals(modeEnum.toString())) {
                return true;
            }
        }
        return false;
    }

}
