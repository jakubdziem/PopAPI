package com.dziem.popapi.service;

import com.dziem.popapi.model.webpage.ActiveUsersStats;

import java.time.LocalDate;
import java.util.List;

public interface ActiveUsersPageService {
    List<ActiveUsersStats> getActiveUsersStatsThisWeek();
    void saveWeeklyActiveUsersStatsSnapshot();

    List<ActiveUsersStats> getActiveUsersStatsFromWeek(String week);

    List<LocalDate> getWeeks();
}
