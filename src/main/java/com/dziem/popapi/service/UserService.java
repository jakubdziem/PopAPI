package com.dziem.popapi.service;

import com.dziem.popapi.model.User;

import java.util.Optional;

public interface UserService {
    String generateUniqueUUID();

    User createAnonimUser(String uuid);

    boolean userExists(String anonimUserId);

    Optional<String> migrateProfileToGoogle(String anonimUserId, String googleId);
}
