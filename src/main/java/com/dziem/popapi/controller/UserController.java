package com.dziem.popapi.controller;

import com.dziem.popapi.model.Mode;
import com.dziem.popapi.model.ScoreDTO;
import com.dziem.popapi.model.StatsDTO;
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

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final StatsService statsService;
    private final ScoreService scoreService;
    @GetMapping("/api/v1/anonim_user_id")
    public String createAnonimUser() {
        String uuid = userService.generateUniqueUUID();
        userService.createAnonimUser(uuid);
        return uuid;
    }
    @PutMapping("api/v1/google/{anonimUserId}/{googleId}")
    public ResponseEntity migrateProfileToGoogle(@PathVariable String anonimUserId, @PathVariable String googleId){
        if(!userService.migrateProfileToGoogle(anonimUserId, googleId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    @PutMapping("/api/v1/{anonimUserId}/{stats}")
    public ResponseEntity updateStatistics(@PathVariable String anonimUserId, @PathVariable String stats) {
        if(!statsService.updateStatistics(anonimUserId, stats)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    @PutMapping("/api/v1/{anonimUserId}/{mode}/{newScore}")
    public ResponseEntity updateBestScore(@PathVariable String anonimUserId, @PathVariable String mode, @PathVariable String newScore) {
        if(!scoreService.checkIsMode(mode)) {
            return new ResponseEntity<>("Mode not found, list of modes:" + Arrays.toString(Mode.values()),HttpStatus.NOT_FOUND);
        }
        if(!scoreService.updateBestScore(anonimUserId, mode, newScore)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    @GetMapping("/api/v1/bestscore/{anonimUserId}")
    public ResponseEntity<List<ScoreDTO>> getBestScoreForUser(@PathVariable String anonimUserId) {
        if(!userService.userExists(anonimUserId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            List<ScoreDTO> bestScoreList = scoreService.getScoreById(anonimUserId);
            return new ResponseEntity<>(bestScoreList,HttpStatus.FOUND);
        }
    }
    @GetMapping("api/v1/stats/{anonimUserId}")
    public ResponseEntity<StatsDTO> getStatsForUser(@PathVariable String anonimUserId) {
        if(!userService.userExists(anonimUserId)) {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(statsService.getStatsByUserId(anonimUserId), HttpStatus.FOUND);
        }
    }
}
