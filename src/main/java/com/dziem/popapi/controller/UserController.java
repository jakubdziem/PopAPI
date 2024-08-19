package com.dziem.popapi.controller;

import com.dziem.popapi.service.ScoreService;
import com.dziem.popapi.service.StatsService;
import com.dziem.popapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final StatsService statsService;
    private final ScoreService scoreService;
    @GetMapping("/api/v1/anonim_user_id")
    public UUID createAnonimUser() {
        UUID uuid = userService.generateUniqueUUID();
        userService.createAnonimUser(uuid);
        return uuid;
    }
    @PutMapping("/api/v1/{anonimUserId}/{stats}")
    public ResponseEntity updateStatistics(@PathVariable UUID anonimUserId, @PathVariable String stats) {
        if(!statsService.updateStatistics(anonimUserId, stats)) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }
    @PutMapping("/api/v1/{anonimUserId}/{mode}/{newScore}")
    public ResponseEntity updateBestScore(@PathVariable UUID anonimUserId, @PathVariable String mode, @PathVariable String newScore) {
        if(!scoreService.updateBestScore(anonimUserId, mode, newScore)) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }
}
