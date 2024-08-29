package com.dziem.popapi.model;

import lombok.Data;

@Data
public class GameStatsDTO {
    private Integer timePlayedSeconds;
    private Integer scoredPoints;
    private String gameMode;
    private boolean wonGame;
}
