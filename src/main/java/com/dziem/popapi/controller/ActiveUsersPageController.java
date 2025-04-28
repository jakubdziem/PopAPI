package com.dziem.popapi.controller;

import com.dziem.popapi.dto.webpage.ActiveUsersStatsDTO;
import com.dziem.popapi.model.webpage.WeeklyActiveUsers;
import com.dziem.popapi.service.ActiveUsersPageService;
import com.dziem.popapi.service.StatsPageChartService;
import com.dziem.popapi.service.StatsPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.time.LocalDate;
import java.util.List;

import static com.dziem.popapi.controller.StatsPageController.ALL_TIME;

@Controller
@RequiredArgsConstructor
@SessionAttributes({"selectedWeekActive"})
public class ActiveUsersPageController {
    private final StatsPageService statsPageService;
    private final StatsPageChartService statsPageChartService;
    private final ActiveUsersPageService activeUsersPageService;
    @ModelAttribute("selectedWeekActive")
    public String initSelectedWeek() {
        return ALL_TIME;
    }
    @GetMapping("/active")
    public String showActiveUsers(Model model, @RequestParam(required = false) String selectedWeekActive) {
        if (selectedWeekActive != null) {
            model.addAttribute("selectedWeekActive", selectedWeekActive);
        }
        model.addAttribute("weeks", activeUsersPageService.getWeeks());
        String week = (String) model.getAttribute("selectedWeekActive");

        if(ALL_TIME.equals(week)) {
            List<ActiveUsersStatsDTO> activeUsersStatsDTOThisWeek = activeUsersPageService.getActiveUsersStatsThisWeek();
            model.addAttribute("activeUsers", activeUsersStatsDTOThisWeek);
            model.addAttribute("activeUsersStatsCombined", statsPageService.getDifferenceStatsOfAllUsersCombined(week));
            int activeUsersCount = activeUsersStatsDTOThisWeek.size();
            int activeOldUsersCount = activeUsersStatsDTOThisWeek.stream().filter(a -> !a.isNewUser()).toList().size();
            model.addAttribute("activeOldUsersCount", activeOldUsersCount);
            model.addAttribute("activeUsersCount", activeUsersCount);
            model.addAttribute("thisWeekUsers", activeUsersCount - activeOldUsersCount);
        } else {
            LocalDate weekDate = LocalDate.parse(week);
            List<ActiveUsersStatsDTO> activeUsersStatsDTOSelectedWeek = activeUsersPageService.getActiveUsersStatsFromWeek(week);
            model.addAttribute("activeUsers", activeUsersStatsDTOSelectedWeek);
            model.addAttribute("activeUsersStatsCombined", statsPageService.getDifferenceStatsOfAllUsersCombined(week));
            WeeklyActiveUsers weeklyActiveUsers = statsPageChartService.getWeeklyActiveUsersForChart().stream()
                    .filter(w -> w.getWeekStartDate().equals(weekDate)).findFirst().orElseGet(
                            WeeklyActiveUsers::new);
            model.addAttribute("activeOldUsersCount", weeklyActiveUsers.getActiveOldUsers());
            model.addAttribute("activeUsersCount", weeklyActiveUsers.getActiveUsers());
            model.addAttribute("thisWeekUsers", weeklyActiveUsers.getActiveNewUsers());
        }

        return "active";
    }
}
