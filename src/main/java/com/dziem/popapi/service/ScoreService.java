package com.dziem.popapi.service;

import com.dziem.popapi.model.Score;
import com.dziem.popapi.model.ScoreDTO;
import com.dziem.popapi.model.User;

import java.util.UUID;

public interface ScoreService {
    Score initializeScore(String mode, User user);

    boolean updateBestScore(UUID uuid, String mode, String newScore);

    ScoreDTO getScoreByIdAndMode(UUID anonimUserId, String mode);
}
