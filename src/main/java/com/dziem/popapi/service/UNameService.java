package com.dziem.popapi.service;

import com.dziem.popapi.model.User;
import com.dziem.popapi.model.UName;

import java.time.LocalDateTime;

public interface UNameService {
    String generateRandomUserName();
    @Deprecated
    UName initializeUserName(User user);
    boolean validateUserName(String name);

    String setUserName(String userId, String name);
    LocalDateTime howLongToChangingName(String userId);
}
