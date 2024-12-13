package com.dziem.popapi.controller;

import com.dziem.popapi.model.webpage.ActiveUsersStats;
import com.dziem.popapi.service.ActiveUsersPageService;
import com.dziem.popapi.service.StatsPageChartService;
import com.dziem.popapi.service.StatsPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static com.dziem.popapi.controller.StatsPageController.ALL_TIME;

@Controller
@RequiredArgsConstructor
public class ActiveUsersPageController {
    private final StatsPageService statsPageService;
    private final StatsPageChartService statsPageChartService;
    private final ActiveUsersPageService activeUsersPageService;
    @GetMapping("/active")
    public String showActiveUsers(Model model) {
        List<ActiveUsersStats> activeUsersStatsThisWeek = activeUsersPageService.getActiveUsersStatsThisWeek();
        model.addAttribute("activeUsers", activeUsersStatsThisWeek);
        model.addAttribute("activeUsersStatsCombined", statsPageService.getDifferenceStatsOfAllUsersCombined(ALL_TIME));
        int activeUsersCount = activeUsersStatsThisWeek.size();
        int activeOldUsersCount = activeUsersStatsThisWeek.stream().filter(a -> !a.isNew()).toList().size();
        model.addAttribute("activeOldUsersCount", activeOldUsersCount);
        model.addAttribute("activeUsersCount", activeUsersCount);
        model.addAttribute("thisWeekUsers", statsPageService.getDifferenceUsersSummed(ALL_TIME));
        return "active";
    }
}
