package com.dziem.popapi.service;

import com.dziem.popapi.model.Stats;
import com.dziem.popapi.model.User;
import com.dziem.popapi.repository.StatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
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
    public boolean updateStatistics(UUID uuid, String stats) {
        AtomicBoolean result = new AtomicBoolean(false);
        statsRepository.findById(uuid).ifPresentOrElse(existing -> {
            result.set(true);
            String[] split = stats.split(";");
            existing.setTotalGamePlayed(existing.getTotalGamePlayed()+1L);
            existing.setAvgScore(new BigDecimal(split[0]));
            existing.setTimePlayed(existing.getTimePlayed()+Long.parseLong(split[1]));
            existing.setTotalScoredPoints(existing.getTotalScoredPoints()+Long.parseLong(split[2]));
            if(split[3].equals("y"))
                existing.setNumberOfWonGames( existing.getNumberOfWonGames() + 1);
            statsRepository.save(existing);
        }, () -> result.set(false));
        return result.get();
    }

}
