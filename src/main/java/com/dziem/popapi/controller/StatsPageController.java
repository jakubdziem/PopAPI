package com.dziem.popapi.controller;

import com.dziem.popapi.model.Mode;
import com.dziem.popapi.model.webpage.StatsWithUName;
import com.dziem.popapi.service.StatsPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class StatsPageController {
    private final StatsPageService statsPageService;
    @GetMapping("/stats")
    public String getStatsPage() {
        return "stats";
    }
    @ModelAttribute("overallStats")
    public List<StatsWithUName> getStatsWithUNameOfAllUsers() {
        return statsPageService.getStatsWithUNameOfAllUsers();
    }
    @ModelAttribute("overallStatsOfUsersCombined")
    public StatsWithUName getStatsOfAllUsersCombined() {
        return statsPageService.getStatsOfAllUsersCombined();
    }
    @GetMapping("/stats_for_chart")
    @ResponseBody
    public List<StatsWithUName> getStatsWithUNameOfAllUsersForChart() {
        return statsPageService.getStatsWithUNameOfAllUsers();
    }
    @ModelAttribute("overallStatsPerMode")
    public Map<Mode, List<StatsWithUName>> getAllGameStatsWithUNameOfAllUsers()
    {
        return statsPageService.getAllGameStatsWithUNameOfAllUsers();
    }
    @ModelAttribute("overallStatsOfUsersPerMode")
    public Map<Mode, StatsWithUName> getGameStatsOffAllUsersCombined() {
        return statsPageService.getGameStatsOffAllUsersCombined();
    }

}
