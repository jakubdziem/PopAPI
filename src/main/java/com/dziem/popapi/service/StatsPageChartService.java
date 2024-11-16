package com.dziem.popapi.service;

import com.dziem.popapi.model.webpage.DailyStatsSummed;

import java.time.LocalDate;
import java.util.List;

public interface StatsPageChartService {
    void saveDailySummedStatsSnapshot();
    void populateWeek(LocalDate week);
    List<DailyStatsSummed> getDailyStatsSummedForChartPerMode(String mode);
    void saveDailySummedStatsFirst();
}
