package com.dziem.popapi.model.webpage;

import lombok.Data;

@Data
public class GameStatsWithUName {
    private String userId;
    private Integer timePlayedSeconds;
    private Integer scoredPoints;
    private String gameMode;
    private boolean wonGame;
    private String name;
}
