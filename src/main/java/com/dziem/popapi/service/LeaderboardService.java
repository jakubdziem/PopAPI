package com.dziem.popapi.service;

import com.dziem.popapi.model.Leaderboard;
import com.dziem.popapi.model.LeaderboardDTO;
import com.dziem.popapi.model.RankScoreDTO;
import com.dziem.popapi.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface LeaderboardService {
    ResponseEntity<List<LeaderboardDTO>> getLeaderboard(String mode);

    List<Leaderboard> initializeLeaderboard(String userId, User user);

    Optional<RankScoreDTO> getRankOfUserInMode(String userId, String mode);
}
