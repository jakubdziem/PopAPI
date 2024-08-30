package com.dziem.popapi.service;

import com.dziem.popapi.model.*;

import java.util.List;

public interface ModeStatsService {
    ModeStats initializeModeStats(User user, String mode);
    boolean updateStatistics(String uuid, String stats);
    List<ModeStatsDTO> getStatsByUserId(String userId);
    boolean updateStatisticsMultipleInput(String userId, List<GameStatsDTO> gameStatsDTOS);
}
