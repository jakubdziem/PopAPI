package com.dziem.popapi.controller;

import com.dziem.popapi.dto.LeaderboardDTO;
import com.dziem.popapi.dto.RankScoreDTO;
import com.dziem.popapi.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class LeaderboardController {
    private final LeaderboardService leaderboardService;
    @GetMapping("/api/v1/leaderboard/{mode}")
    public ResponseEntity<List<LeaderboardDTO>> getLeaderboard(@PathVariable String mode) {
        List<LeaderboardDTO> leaderboardFirst200 = leaderboardService.getLeaderboardFirst200(mode);
        if(leaderboardFirst200.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(leaderboardFirst200, HttpStatus.ACCEPTED);
    }
    @GetMapping("api/v1/rank/{userId}/{mode}")
    public ResponseEntity<RankScoreDTO> getRankOfUserInMode(@PathVariable String userId, @PathVariable String mode) {
        Optional<RankScoreDTO> rankScoreDTO = leaderboardService.getRankOfUserInMode(userId, mode);
        return rankScoreDTO.map(scoreDTO -> new ResponseEntity<>(scoreDTO, HttpStatus.ACCEPTED)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
