package com.dziem.popapi.service;

import com.dziem.popapi.model.Mode;
import com.dziem.popapi.model.Score;
import com.dziem.popapi.model.User;

import java.util.UUID;

public interface ScoreService {
    Score initializeScore(Mode mode, User user);

    boolean updateBestScore(UUID uuid, String mode, String newScore);
}
