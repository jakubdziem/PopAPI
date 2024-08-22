package com.dziem.popapi.service;

import com.dziem.popapi.model.Score;
import com.dziem.popapi.model.ScoreDTO;
import com.dziem.popapi.model.User;

import java.util.List;
import java.util.UUID;

public interface ScoreService {
    Score initializeScore(String mode, User user);

    boolean updateBestScore(UUID uuid, String mode, String newScore);

    ScoreDTO getScoreByUserIdAndMode(UUID anonimUserId, String mode);

    List<ScoreDTO> getScoreById(UUID anonimUserId);
    boolean checkIsMode(String mode);
}
