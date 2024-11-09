package com.dziem.popapi.controller;

import com.dziem.popapi.model.Mode;
import com.dziem.popapi.model.webpage.StatsWithUName;
import com.dziem.popapi.model.webpage.UsersSummed;
import com.dziem.popapi.service.StatsPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class StatsPageController {
    private final StatsPageService statsPageService;
    @ModelAttribute("overallStatsOfUsersCombined")
    public StatsWithUName getStatsOfAllUsersCombined() {
        return statsPageService.getStatsOfAllUsersCombinedCurrent();
    }
    @GetMapping("/stats_for_chart")
    @ResponseBody
    public List<StatsWithUName> getStatsWithUNameOfAllUsersForChart() {
        return statsPageService.getStatsWithUNameOfAllUsersCurrent();
    }
    @ModelAttribute("overallStatsOfUsersPerMode")
    public Map<String, StatsWithUName> getGameStatsOffAllUsersCombined() {
        return statsPageService.getGameStatsOffAllUsersCombinedCurrent();
    }

    @GetMapping("/stats")
    public String showStats(@RequestParam(defaultValue = "GENERAL") String selectedMode, Model model) {
        model.addAttribute("selectedMode", selectedMode);
        List<String> modes = Arrays.stream(Mode.values()).map(Enum::toString).toList();
        model.addAttribute("modes", modes);

        if ("GENERAL".equals(selectedMode)) {
            model.addAttribute("overallStats", statsPageService.getStatsWithUNameOfAllUsersCurrent());
        } else {
            model.addAttribute("overallStatsPerMode", statsPageService.getAllGameStatsWithUNameOfAllUsersCurrent());
        }
        return "stats";
    }

    @ModelAttribute("users")
    public UsersSummed getUsersSummed() {
        return statsPageService.getUsersSummedCurrent();
    }
}
