package com.dziem.popapi.service;

import com.dziem.popapi.model.webpage.DailyStatsSummed;
import com.dziem.popapi.model.webpage.DailyUsersSummed;
import com.dziem.popapi.model.webpage.DailyUsersSummedBoth;

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
    List<DailyUsersSummedBoth> getDailyUsersSummedForChart();
    DailyUsersSummed getTodayUserSummedDifference();
    void saveDailySummedStatsFirst();

}
