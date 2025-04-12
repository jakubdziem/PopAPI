package com.dziem.popapi.service;

import com.dziem.popapi.model.User;
import com.dziem.popapi.model.UName;

import java.time.LocalDateTime;

public interface UNameService {
    String generateRandomUsername();
    @Deprecated
    UName initializeUserName(User user);
    boolean validateUsername(String name);

    String setUsername(String userId, String name);
    LocalDateTime howLongToChangingName(String userId);
}
