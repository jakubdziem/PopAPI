package com.dziem.popapi.service;

import com.dziem.popapi.dto.webpage.StatsWithUNameDTO;
import com.dziem.popapi.dto.webpage.UsersSummedDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface StatsPageService {
    String COMBINED_STATS = "COMBINED_STATS";
    String STATS_OFF_ALL_USERS = "STATS_OFF_ALL_USERS";
    List<StatsWithUNameDTO> getStatsWithUNameOfAllUsersCurrent();
    Map<String, List<StatsWithUNameDTO>> getAllGameStatsWithUNameOfAllUsersCurrent();
    StatsWithUNameDTO getStatsOfAllUsersCombinedCurrent();
    Map<String, StatsWithUNameDTO> getGameStatsOffAllUsersCombinedCurrent();

    UsersSummedDTO getUsersSummedCurrent();

    List<StatsWithUNameDTO> getStatsWithUNameOfAllUsersFromWeek(LocalDate week);
    Map<String, List<StatsWithUNameDTO>> getAllGameStatsWithUNameOfAllUsersFromWeek(LocalDate week);
    StatsWithUNameDTO getStatsOfAllUsersCombinedFromWeek(LocalDate week);
    Map<String, StatsWithUNameDTO> getGameStatsOffAllUsersCombinedFromWeek(LocalDate week);

    UsersSummedDTO getUsersSummedFromWeek(LocalDate week);
    void saveWeeklyStatsSnapshot();
    List<LocalDate> getWeeks();
    UsersSummedDTO getDifferenceUsersSummed(String weekStr);
    Map<String, StatsWithUNameDTO> getDifferenceGameStatsOffAllUsersCombined(String weekStr);
    StatsWithUNameDTO getDifferenceStatsOfAllUsersCombined(String weekStr);
    Map<String, Boolean> getModesWithPositiveDifference(String week, List<String> modes);
}
