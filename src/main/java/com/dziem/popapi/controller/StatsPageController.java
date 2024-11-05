package com.dziem.popapi.controller;

import com.dziem.popapi.model.webpage.StatsWithUName;
import com.dziem.popapi.service.StatsPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

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
}
