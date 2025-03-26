package com.dziem.popapi.controller;

import com.dziem.popapi.model.Mode;
import com.dziem.popapi.model.webpage.StatsWithUName;
import com.dziem.popapi.service.StatsPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.dziem.popapi.controller.StatsPageController.ALL_TIME;
import static com.dziem.popapi.service.StatsPageService.COMBINED_STATS;

@Controller
@RequiredArgsConstructor
@SessionAttributes({"selectedWeek", "selectedMode", "attributeSelect"})
public class AllUsersPageController {

    private final StatsPageService statsPageService;

    @ModelAttribute("selectedWeek")
    public String initSelectedWeek() {
        return ALL_TIME;
    }
    @ModelAttribute("selectedMode")
    public String initSelectedMode() {
        return COMBINED_STATS;
    }
    @ModelAttribute("attributeSelect")
    public String initAttributeSelect() {
        return "totalGamePlayed";
    }

    @GetMapping("/allUsers")
    public String showUsers(@RequestParam(required = false) String selectedMode, @RequestParam(required = false) String selectedWeek, @RequestParam(required = false) String attributeSelect, Model model) {
        if (selectedWeek != null) {
            model.addAttribute("selectedWeek", selectedWeek);
        }
        if (selectedMode != null) {
            model.addAttribute("selectedMode", selectedMode);
        }
        if (attributeSelect != null) {
            model.addAttribute("attributeSelect", attributeSelect);
        }
        List<String> modes = Arrays.stream(Mode.values()).map(Enum::toString).toList();
        model.addAttribute("modes", modes);
        model.addAttribute("weeks", statsPageService.getWeeks());
        String week = (String) model.getAttribute("selectedWeek");
        String mode = (String) model.getAttribute("selectedMode");
        model.addAttribute("differenceUsersSummed", statsPageService.getDifferenceUsersSummed(week));

        Map<String, Boolean> modesWithPositiveDifference = modes.stream().collect(Collectors.toMap(
                m -> m,
                m -> {
                    StatsWithUName difference = statsPageService.getDifferenceGameStatsOffAllUsersCombined(week).get(m);
                    return difference != null && difference.getTotalGamePlayed() != null && difference.getTotalGamePlayed() > 0;
                }
        ));
        model.addAttribute("modesWithPositiveDifference", modesWithPositiveDifference);

        if(ALL_TIME.equals(week)) {
            if (COMBINED_STATS.equals(mode)) {
                model.addAttribute("overallStats", statsPageService.getStatsWithUNameOfAllUsersCurrent());
            } else {
                model.addAttribute("overallStats", statsPageService.getAllGameStatsWithUNameOfAllUsersCurrent());
            }
        } else {
            LocalDate weekDate = LocalDate.parse(week);
            if (COMBINED_STATS.equals(mode)) {
                model.addAttribute("overallStats", statsPageService.getStatsWithUNameOfAllUsersFromWeek(weekDate));
            } else {
                model.addAttribute("overallStats", statsPageService.getAllGameStatsWithUNameOfAllUsersFromWeek(weekDate));
            }
        }
        return "allUsers";
    }
}
