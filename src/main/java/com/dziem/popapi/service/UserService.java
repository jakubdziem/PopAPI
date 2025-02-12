package com.dziem.popapi.service;

import com.dziem.popapi.model.User;

import java.util.Optional;

public interface UserService {
    String generateUniqueUUID();

    User createUser(String uuid, boolean guest);

    boolean userExists(String userId);

    Optional<String> migrateProfileToGoogle(String anonimUserId, String googleId);

    Optional<String> createGoogleUser(String googleId);
    void addNewModesToUsers();
}
