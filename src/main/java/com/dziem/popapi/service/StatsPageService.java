package com.dziem.popapi.service;

import com.dziem.popapi.model.Mode;
import com.dziem.popapi.model.webpage.StatsWithUName;

import java.util.List;
import java.util.Map;

public interface StatsPageService {
    List<StatsWithUName> getStatsWithUNameOfAllUsers();
    List<StatsWithUName> getGameStatsWithUNameOfAllUsers();
    StatsWithUName getStatsOfAllUsersCombined();
    Map<Mode, StatsWithUName> getGameStatsOffAllUsersCombined();
}
