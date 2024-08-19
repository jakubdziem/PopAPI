package com.dziem.popapi.service;

import com.dziem.popapi.model.Stats;
import com.dziem.popapi.model.User;

import java.util.UUID;

public interface StatsService {
    Stats initializeStats(User user);
    boolean updateStatistics(UUID uuid, String stats);
}
