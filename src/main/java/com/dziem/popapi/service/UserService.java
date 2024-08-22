package com.dziem.popapi.service;

import com.dziem.popapi.model.User;

import java.util.UUID;
public interface UserService {
    UUID generateUniqueUUID();

    User createAnonimUser(UUID uuid);

    boolean userExists(UUID anonimUserId);

    boolean migrateProfileToGoogle(UUID anonimUserId, String googleId);
}
