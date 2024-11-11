package com.dziem.popapi.service;

import com.dziem.popapi.model.webpage.StatsWithUName;
import com.dziem.popapi.model.webpage.UsersSummed;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface StatsPageService {
    String COMBINED_STATS = "COMBINED_STATS";
    String STATS_OFF_ALL_USERS = "STATS_OFF_ALL_USERS";
    List<StatsWithUName> getStatsWithUNameOfAllUsersCurrent();
    Map<String, List<StatsWithUName>> getAllGameStatsWithUNameOfAllUsersCurrent();
    StatsWithUName getStatsOfAllUsersCombinedCurrent();
    Map<String, StatsWithUName> getGameStatsOffAllUsersCombinedCurrent();

    UsersSummed getUsersSummedCurrent();

    List<StatsWithUName> getStatsWithUNameOfAllUsersFromWeek(LocalDate week);
    Map<String, List<StatsWithUName>> getAllGameStatsWithUNameOfAllUsersFromWeek(LocalDate week);
    StatsWithUName getStatsOfAllUsersCombinedFromWeek(LocalDate week);
    Map<String, StatsWithUName> getGameStatsOffAllUsersCombinedFromWeek(LocalDate week);

    UsersSummed getUsersSummedFromWeek(LocalDate week);
    void saveWeeklyStatsSnapshot();
    List<LocalDate> getWeeks();
    UsersSummed getDifferenceUsersSummed(String weekStr);
    Map<String, StatsWithUName> getDifferenceGameStatsOffAllUsersCombined(String weekStr);
    StatsWithUName getDifferenceStatsOfAllUsersCombined(String weekStr);
}
