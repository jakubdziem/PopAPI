package com.dziem.popapi.service;

import com.dziem.popapi.model.Leaderboard;
import com.dziem.popapi.dto.LeaderboardDTO;
import com.dziem.popapi.dto.RankScoreDTO;
import com.dziem.popapi.model.User;

import java.util.List;
import java.util.Optional;

public interface LeaderboardService {
    List<LeaderboardDTO> getLeaderboardFirst200(String mode);

    List<Leaderboard> initializeLeaderboard(String userId, User user);

    Optional<RankScoreDTO> getRankOfUserInMode(String userId, String mode);
    List<LeaderboardDTO> getLeaderboard(String mode);
}
