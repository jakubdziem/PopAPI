package com.dziem.popapi.service;

import com.dziem.popapi.model.User;
import com.dziem.popapi.model.UName;

public interface UNameService {
    String generateRandomUserName();

    UName initializeUserName(User user);
    boolean validateUserName(String name);

    boolean setUserName(String userId, String name);
}
