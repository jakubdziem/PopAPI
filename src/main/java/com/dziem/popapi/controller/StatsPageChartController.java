package com.dziem.popapi.controller;

import com.dziem.popapi.model.webpage.DailyActiveUsers;
import com.dziem.popapi.dto.webpage.DailyUsersSummedBothDTO;
import com.dziem.popapi.model.webpage.*;
import com.dziem.popapi.service.StatsPageChartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@AllArgsConstructor
public class StatsPageChartController {
    private final StatsPageChartService statsPageChartService;
    @GetMapping("/stats_for_chart/{mode}")
    @ResponseBody
    public List<DailyStatsSummed> getStatsWithUNameOfAllUsersForChart(@PathVariable String mode) {
        return statsPageChartService.getDailyStatsSummedForChartPerMode(mode);
    }
    @GetMapping("/stats_for_chart_new_users")
    @ResponseBody
    public List<DailyUsersSummedBothDTO> getUsersSummedForChart() {
        return statsPageChartService.getDailyUsersSummedForChart();
    }
    @GetMapping("/stats_for_chart_new_users_weekly")
    @ResponseBody
    public List<WeeklyNewUsersSummed> getWeeklyNewUsersSummedForChart() {
        return statsPageChartService.getWeeklyNewUsersSummedForChart();
    }
    @GetMapping("/stats_for_chart_active_users")
    @ResponseBody
    public List<DailyActiveUsers> getDailyActiveUsersForChart() {
        return statsPageChartService.getDailyActiveUsersForChart();
    }
    @GetMapping("/stats_for_chart_active_users_weekly")
    @ResponseBody
    public List<WeeklyActiveUsers> getWeeklyActiveUsersForChart() {
        return statsPageChartService.getWeeklyActiveUsersForChart();
    }
}
