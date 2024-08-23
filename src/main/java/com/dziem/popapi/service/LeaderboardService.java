package com.dziem.popapi.service;

import com.dziem.popapi.model.LeaderboardDTO;

import java.util.List;

public interface LeaderboardService {
    List<LeaderboardDTO> getLeaderboard(String mode);

//    List<Leaderboard> initializeLeaderboard(String userId, User user);
}
