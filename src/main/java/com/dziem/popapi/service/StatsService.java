package com.dziem.popapi.service;

import com.dziem.popapi.model.Stats;
import com.dziem.popapi.model.StatsDTO;
import com.dziem.popapi.model.User;


public interface StatsService {
    Stats initializeStats(User user);
    boolean updateStatistics(String uuid, String stats);
    StatsDTO getStatsByUserId(String anonimUserId);
}
