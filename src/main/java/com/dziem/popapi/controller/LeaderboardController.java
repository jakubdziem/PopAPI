package com.dziem.popapi.controller;

import com.dziem.popapi.model.LeaderboardDTO;
import com.dziem.popapi.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LeaderboardController {
    private final LeaderboardService leaderboardService;
    @GetMapping("/api/v1/leaderboard/{mode}")
    public List<LeaderboardDTO> getLeaderboard(@PathVariable String mode) {
        return leaderboardService.getLeaderboard(mode);
    }
}
