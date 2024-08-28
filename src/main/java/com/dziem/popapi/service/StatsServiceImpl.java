package com.dziem.popapi.service;

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
            existing.setTotalGamePlayed(existing.getTotalGamePlayed()+Long.parseLong(split[0]));
            existing.setTimePlayed(existing.getTimePlayed()+Long.parseLong(split[1]));
            long scoredPoints = Long.parseLong(split[2]);
            existing.setTotalScoredPoints(existing.getTotalScoredPoints()+ scoredPoints);
            if(split[3].equals("y"))
                existing.setNumberOfWonGames( existing.getNumberOfWonGames() + 1);
            existing.setAvgScore(calculateAvgScore(existing.getTotalScoredPoints(), existing.getTotalGamePlayed()));
            String currentMode = split[4];
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
    public StatsDTO getStatsByUserId(String anonimUserId) {
        return statsMapper.statsToStatsDTO(statsRepository.findById(anonimUserId).get());
        //it is checked if its present in controller
    }
    private BigDecimal calculateAvgScore(Long totalScoredPoints, Long totalGamePlayed) {
        BigDecimal scoredPoints = new BigDecimal(totalScoredPoints);
        return scoredPoints.divide(new BigDecimal(totalGamePlayed),2,RoundingMode.HALF_UP);
    }
}
