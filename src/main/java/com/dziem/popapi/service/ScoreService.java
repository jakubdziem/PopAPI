package com.dziem.popapi.service;

import com.dziem.popapi.model.Score;
import com.dziem.popapi.model.ScoreDTO;
import com.dziem.popapi.model.User;

import java.util.List;

public interface ScoreService {
    Score initializeScore(String mode, User user);

    boolean updateBestScore(String uuid, String mode, String newScore);
    @Deprecated

    ScoreDTO getScoreByUserIdAndMode(String anonimUserId, String mode);

    List<ScoreDTO> getScoreById(String anonimUserId);
    boolean checkIsMode(String mode);
}
