package com.dziem.popapi.service;

import com.dziem.popapi.model.User;
import com.dziem.popapi.model.UName;

public interface UNameService {
    String generateRandomUserName();

    UName initializeUserName(User user);
}
