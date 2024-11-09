package com.dziem.popapi.controller;

import com.dziem.popapi.model.Mode;
import com.dziem.popapi.model.webpage.StatsWithUName;
import com.dziem.popapi.service.StatsPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@SessionAttributes({"selectedWeek", "selectedMode"})
public class StatsPageController {
    private final StatsPageService statsPageService;
    @GetMapping("/stats_for_chart")
    @ResponseBody
    public List<StatsWithUName> getStatsWithUNameOfAllUsersForChart() {
        return statsPageService.getStatsWithUNameOfAllUsersCurrent();
    }
    @ModelAttribute("selectedWeek")
    public String initSelectedWeek() {
        return "ALL_TIME";
    }
    @ModelAttribute("selectedMode")
    public String initSelectedMode() {
        return "COMBINED_STATS";
    }

    @GetMapping("/stats")
    public String showStats(@RequestParam(required = false) String selectedMode, @RequestParam(required = false) String selectedWeek, Model model) {
        if (selectedWeek != null) {
            model.addAttribute("selectedWeek", selectedWeek);
        }
        if (selectedMode != null) {
            model.addAttribute("selectedMode", selectedMode);
        }
        List<String> modes = Arrays.stream(Mode.values()).map(Enum::toString).toList();
        model.addAttribute("modes", modes);
        model.addAttribute("weeks", statsPageService.getWeeks());
        String week = (String) model.getAttribute("selectedWeek");
        String mode = (String) model.getAttribute("selectedMode");
        model.addAttribute("differenceUsersSummed", statsPageService.getDifferenceUsersSummed(week));
        if("ALL_TIME".equals(week)) {
            model.addAttribute("users",statsPageService.getUsersSummedCurrent());
            if ("COMBINED_STATS".equals(mode)) {
                model.addAttribute("overallStats", statsPageService.getStatsWithUNameOfAllUsersCurrent());
                model.addAttribute("overallStatsOfUsersCombined", statsPageService.getStatsOfAllUsersCombinedCurrent());
            } else {
                model.addAttribute("overallStats", statsPageService.getAllGameStatsWithUNameOfAllUsersCurrent());
                model.addAttribute("overallStatsOfUsersCombined", statsPageService.getGameStatsOffAllUsersCombinedCurrent());
            }
        } else {
            LocalDate weekDate = LocalDate.parse(week);
            if ("COMBINED_STATS".equals(mode)) {
                model.addAttribute("overallStats", statsPageService.getStatsWithUNameOfAllUsersFromWeek(weekDate));
                model.addAttribute("overallStatsOfUsersCombined", statsPageService.getStatsOfAllUsersCombinedFromWeek(weekDate));
            } else {
                model.addAttribute("overallStats", statsPageService.getAllGameStatsWithUNameOfAllUsersFromWeek(weekDate));
                model.addAttribute("overallStatsOfUsersCombined", statsPageService.getGameStatsOffAllUsersCombinedFromWeek(weekDate));
            }
            model.addAttribute("users", statsPageService.getUsersSummedFromWeek(weekDate));
        }
        return "stats";
    }
}
