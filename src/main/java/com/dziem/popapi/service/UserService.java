package com.dziem.popapi.service;

import com.dziem.popapi.model.User;

public interface UserService {
    String generateUniqueUUID();

    User createAnonimUser(String uuid);

    boolean userExists(String anonimUserId);

    boolean migrateProfileToGoogle(String anonimUserId, String googleId);
}
