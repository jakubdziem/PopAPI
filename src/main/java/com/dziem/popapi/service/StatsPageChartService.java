package com.dziem.popapi.service;

import com.dziem.popapi.model.webpage.DailyActiveUsers;
import com.dziem.popapi.dto.webpage.DailyUsersSummedBothDTO;
import com.dziem.popapi.model.webpage.*;

import java.time.LocalDate;
import java.util.List;

public interface StatsPageChartService {
    void saveDailyStatsSummedSnapshot();
    void saveDailySummedUsersSnapshot();
    void saveWeeklyNewUsersSummed();
    void saveWeeklyActiveUsers();
    void saveDailyActiveUsers();
    void populateWeek(LocalDate week);
    List<DailyStatsSummed> getDailyStatsSummedForChartPerMode(String mode);
    List<DailyUsersSummedBothDTO> getDailyUsersSummedForChart();
    DailyUsersSummed getTodayUserSummedDifference();
    List<DailyActiveUsers> getDailyActiveUsersForChart();
    List<WeeklyNewUsersSummed> getWeeklyNewUsersSummedForChart();
    List<WeeklyActiveUsers> getWeeklyActiveUsersForChart();
    void saveDailySummedStatsFirst();

}
