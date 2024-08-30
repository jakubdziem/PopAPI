package com.dziem.popapi.controller;

import com.dziem.popapi.mapper.UserMapper;
import com.dziem.popapi.model.*;
import com.dziem.popapi.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final StatsService statsService;
    private final ScoreService scoreService;
    private final UNameService uNameService;
    private final UserMapper userMapper;
    private final ModeStatsService modeStatsService;
    @PostMapping("api/v1/google/{googleId}")
    public ResponseEntity createGoogleUser(@PathVariable String googleId) {
        AtomicReference<ResponseEntity> atomicReference = new AtomicReference<>();
        userService.createGoogleUser(googleId).ifPresentOrElse(
                username -> atomicReference.set(new ResponseEntity(username,HttpStatus.ACCEPTED)),
                () -> atomicReference.set(new ResponseEntity(HttpStatus.CONFLICT))
        );
        return atomicReference.get();
    }
    @GetMapping("/api/v1/anonim_user_id")
    public UserDTO createAnonimUser() {
        String uuid = userService.generateUniqueUUID();
        return userMapper.userToUserDTO(userService.createUser(uuid, true));
    }
    @PutMapping("api/v1/google/{anonimUserId}/{googleId}")
    public ResponseEntity migrateProfileToGoogle(@PathVariable String anonimUserId, @PathVariable String googleId){
        AtomicReference<ResponseEntity> atomicReference = new AtomicReference<>();
        if(userService.userExists(googleId)) {
            return new ResponseEntity("This google account already exists in database.",HttpStatus.CONFLICT); //409
        }
        userService.migrateProfileToGoogle(anonimUserId, googleId).ifPresentOrElse(
                username -> atomicReference.set(new ResponseEntity(username,HttpStatus.ACCEPTED)),
                () -> atomicReference.set(new ResponseEntity(HttpStatus.NOT_FOUND)));
        return atomicReference.get();
    }
    @PutMapping("api/v1/set_name/{googleId}/{name}")
    public ResponseEntity setUserNameForGoogleUser(@PathVariable String googleId, @PathVariable String name) {
        if(!uNameService.validateUserName(name)) {
            return new ResponseEntity("Contained restricted word.",HttpStatus.BAD_REQUEST); //400
        }
        if(!uNameService.setUserName(googleId, name)) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }
    @PutMapping("/api/v1/{anonimUserId}/{stats}")
    public ResponseEntity updateStatistics(@PathVariable String anonimUserId, @PathVariable String stats) {
        if(!statsService.updateStatistics(anonimUserId, stats)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            modeStatsService.updateStatistics(anonimUserId, stats);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    @PutMapping(value = "/api/v1/statsjson/{userId}", consumes = "application/json")
    public ResponseEntity updateStatisticsMultipleInput(@PathVariable String userId, @RequestBody List<GameStatsDTO> gameStatsDTOS) {
        if(!statsService.updateStatisticsMultipleInput(userId, gameStatsDTOS)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            modeStatsService.updateStatisticsMultipleInput(userId, gameStatsDTOS);
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
            return new ResponseEntity<>(bestScoreList,HttpStatus.ACCEPTED);
        }
    }
    @GetMapping("api/v1/stats/{userId}")
    public ResponseEntity<StatsDTO> getStatsForUser(@PathVariable String userId) {
        if(!userService.userExists(userId)) {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(statsService.getStatsByUserId(userId), HttpStatus.ACCEPTED);
        }
    }
    @GetMapping("api/v1/all_stats/{userId}")
    public ResponseEntity<List<ModeStatsDTO>> getStatsForUserAll(@PathVariable String userId) {
        if(!userService.userExists(userId)) {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(modeStatsService.getStatsByUserId(userId), HttpStatus.ACCEPTED);
        }
    }
}
