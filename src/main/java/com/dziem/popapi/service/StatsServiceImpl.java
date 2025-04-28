package com.dziem.popapi.service;

import com.dziem.popapi.dto.GameStatsDTO;
import com.dziem.popapi.dto.StatsDTO;
import com.dziem.popapi.mapper.StatsMapper;
import com.dziem.popapi.model.*;
import com.dziem.popapi.repository.StatsRepository;
import com.dziem.popapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    private final StatsMapper statsMapper;
    private final UserRepository userRepository;
    private final ScoreService scoreService;

    @Override
    public Stats initializeStats(User user) {
        Stats stats = new Stats();
        stats.setUser(user);
        stats.setTotalGamePlayed(0L);
        stats.setTimePlayed(0L);
        stats.setAvgScore(new BigDecimal("0.0"));
        stats.setTotalScoredPoints(0L);
        stats.setNumberOfWonGames(0);
        return statsRepository.save(stats);
    }

    @Override
    public boolean updateStatistics(String userId, String stats) {
        AtomicBoolean result = new AtomicBoolean(false);
        statsRepository.findById(userId).ifPresentOrElse(existing -> {
            result.set(true);
            String[] split = stats.split(",");
            existing.setTotalGamePlayed(existing.getTotalGamePlayed()+1L);
            existing.setTimePlayed(existing.getTimePlayed()+Long.parseLong(split[0]));
            long scoredPoints = Long.parseLong(split[1]);
            existing.setTotalScoredPoints(existing.getTotalScoredPoints()+ scoredPoints);
            if(split[2].equals("y"))
                existing.setNumberOfWonGames( existing.getNumberOfWonGames() + 1);
            existing.setAvgScore(calculateAvgScore(existing.getTotalScoredPoints(), existing.getTotalGamePlayed()));
            String currentMode = split[3];
            List<Score> bestScores = userRepository.findById(userId).get().getBestScores();
            for(Score score : bestScores) {
                if(currentMode.equals(score.getMode()) && scoredPoints > score.getBestScore()) {
                    scoreService.updateBestScore(userId, currentMode, String.valueOf(scoredPoints));
                    break;
                }
            }
            statsRepository.save(existing);
        }, () -> result.set(false));
        return result.get();
    }

    @Override
    public StatsDTO getStatsByUserId(String userId) {
        return statsMapper.statsToStatsDTO(statsRepository.findById(userId).get());
        //it is checked if its present in controller
    }

    @Override
    public boolean updateStatisticsMultipleInput(String userId, List<GameStatsDTO> gameStatsDTOS) {
        AtomicBoolean result = new AtomicBoolean(false);
        statsRepository.findById(userId).ifPresentOrElse(existing -> {
            for(GameStatsDTO gameStatsDTO : gameStatsDTOS) {
                String stats = gameStatsDTO.getTimePlayedSeconds().toString().concat(",")
                        .concat(gameStatsDTO.getScoredPoints().toString()).concat(",")
                        .concat(gameStatsDTO.isWonGame() ? "y" : "n").concat(",")
                        .concat(gameStatsDTO.getGameMode());
                updateStatistics(userId, stats);
            }
            result.set(true);
        }, () -> result.set(false));
        return result.get();
    }

    protected static BigDecimal calculateAvgScore(Long totalScoredPoints, Long totalGamePlayed) {
        BigDecimal scoredPoints = new BigDecimal(totalScoredPoints);
        return scoredPoints.divide(new BigDecimal(totalGamePlayed),2,RoundingMode.HALF_UP);
    }
}
