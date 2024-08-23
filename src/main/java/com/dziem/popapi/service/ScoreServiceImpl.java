package com.dziem.popapi.service;

import com.dziem.popapi.mapper.ScoreMapper;
import com.dziem.popapi.model.*;
import com.dziem.popapi.repository.LeaderboardRepository;
import com.dziem.popapi.repository.ScoreRepository;
import com.dziem.popapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
@Service
@RequiredArgsConstructor
public class ScoreServiceImpl implements ScoreService {
    private final ScoreRepository scoreRepository;
    private final UserRepository userRepository;
    private final ScoreMapper scoreMapper;
    private final LeaderboardRepository leaderboardRepository;
    @Override
    public Score initializeScore(String mode, User user) {
        Score score = new Score();
        score.setBestScore(0);
        score.setMode(mode);
        score.setUser(user);
        return scoreRepository.save(score);
    }

    @Override
    public boolean updateBestScore(String userId, String mode, String newScore) {
        AtomicBoolean result = new AtomicBoolean(false);
        userRepository.findById(userId).ifPresentOrElse(existing -> {
                    result.set(true);
                    List<Score> bestScores = existing.getBestScores();
                    List<Score> scoreToUpdate = bestScores.stream().filter(score -> score.getMode().equals(mode)).toList();
                    Score score = scoreToUpdate.get(0);
                    score.setBestScore(Integer.parseInt(newScore));
                    if (!existing.isGuest()) {
                        List<Leaderboard> leaderboards = existing.getLeaderboard();
                        for (Leaderboard leaderboard : leaderboards) {
                            if (leaderboard.getMode().equals(mode)) {
                                leaderboard.setScore(Integer.parseInt(newScore));
                                break;
                            }
                        }
                        leaderboardRepository.save(leaderboards.get(0));
                        existing.setLeaderboard(leaderboards);
                    }
            scoreRepository.save(score);
            }, () -> result.set(false));
        return result.get();

    }

    @Override
    public ScoreDTO getScoreByUserIdAndMode(String anonimUserId, String mode) {
        List<Score> bestScores = userRepository.findById(anonimUserId).get().getBestScores();
        for (Score bestScore : bestScores) {
            String currentMode = bestScore.getMode();
            if (currentMode.equals(mode))
                return scoreMapper.scoreToScoreDTO(bestScore);
        }
        return new ScoreDTO();
    }

    @Override
    public List<ScoreDTO> getScoreById(String anonimUserId) {
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
