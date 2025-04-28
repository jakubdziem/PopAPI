package com.dziem.popapi.service;

import com.dziem.popapi.dto.GameStatsDTO;
import com.dziem.popapi.model.Stats;
import com.dziem.popapi.dto.StatsDTO;
import com.dziem.popapi.model.User;

import java.util.List;


public interface StatsService {
    Stats initializeStats(User user);
    boolean updateStatistics(String uuid, String stats);
    StatsDTO getStatsByUserId(String userId);

    boolean updateStatisticsMultipleInput(String userId, List<GameStatsDTO> gameStatsDTOS);
}
