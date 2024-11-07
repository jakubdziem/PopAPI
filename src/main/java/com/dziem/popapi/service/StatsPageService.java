package com.dziem.popapi.service;

import com.dziem.popapi.model.webpage.StatsWithUName;

import java.util.List;
import java.util.Map;

public interface StatsPageService {
    List<StatsWithUName> getStatsWithUNameOfAllUsers();
    Map<String, List<StatsWithUName>> getAllGameStatsWithUNameOfAllUsers();
    StatsWithUName getStatsOfAllUsersCombined();
    Map<String, StatsWithUName> getGameStatsOffAllUsersCombined();
}
