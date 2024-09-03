package com.dziem.popapi.controller;

import com.dziem.popapi.model.LeaderboardDTO;
import com.dziem.popapi.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @GetMapping("api/v1/rank/{userId}/{mode}")
    public ResponseEntity<Integer> getRankOfUserInMode(@PathVariable String userId, @PathVariable String mode) {
        Integer rankOfUserInMode = leaderboardService.getRankOfUserInMode(userId, mode);
        if(rankOfUserInMode > 0L) {
            return new ResponseEntity<>(rankOfUserInMode, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
