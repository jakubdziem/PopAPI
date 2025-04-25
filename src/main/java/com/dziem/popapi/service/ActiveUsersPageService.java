package com.dziem.popapi.service;

import com.dziem.popapi.dto.webpage.ActiveUsersStatsDTO;

import java.time.LocalDate;
import java.util.List;

public interface ActiveUsersPageService {
    List<ActiveUsersStatsDTO> getActiveUsersStatsThisWeek();
    void saveWeeklyActiveUsersStatsSnapshot();

    List<ActiveUsersStatsDTO> getActiveUsersStatsFromWeek(String week);

    List<LocalDate> getWeeks();
}
