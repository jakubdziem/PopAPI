package com.dziem.popapi.controller;

import com.dziem.popapi.model.Mode;
import com.dziem.popapi.model.webpage.StatsWithUName;
import com.dziem.popapi.model.webpage.UsersSummed;
import com.dziem.popapi.service.StatsPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    @ModelAttribute("overallStatsOfUsersCombined")
    public StatsWithUName getStatsOfAllUsersCombined() {
        return statsPageService.getStatsOfAllUsersCombinedCurrent();
    }
    @ModelAttribute("overallStatsOfUsersPerMode")
    public Map<String, StatsWithUName> getGameStatsOffAllUsersCombined() {
        return statsPageService.getGameStatsOffAllUsersCombinedCurrent();
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
        if("ALL_TIME".equals(week)) {
            if ("COMBINED_STATS".equals(mode)) {
                model.addAttribute("overallStats", statsPageService.getStatsWithUNameOfAllUsersCurrent());
            } else {
                model.addAttribute("overallStatsPerMode", statsPageService.getAllGameStatsWithUNameOfAllUsersCurrent());
            }
        } else {
            System.out.println("\n\n\n\n");
            System.out.println(week);
            System.out.println("\n\n\n\n");
            LocalDate weekDate = LocalDate.parse(week);
            if ("COMBINED_STATS".equals(mode)) {
                model.addAttribute("overallStats", statsPageService.getStatsWithUNameOfAllUsersFromWeek(weekDate));
            } else {
                model.addAttribute("overallStatsPerMode", statsPageService.getAllGameStatsWithUNameOfAllUsersFromWeek(weekDate));
            }
        }
        return "stats";
    }

    @ModelAttribute("users")
    public UsersSummed getUsersSummed() {
        return statsPageService.getUsersSummedCurrent();
    }
    @ModelAttribute("usersWeekly")
    public UsersSummed getUsersSummedFromWeek() {
        return statsPageService.getUsersSummedFromWeek(LocalDate.parse("2024-11-09"));
    }
}
