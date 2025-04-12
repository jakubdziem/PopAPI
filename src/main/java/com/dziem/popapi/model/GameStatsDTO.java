package com.dziem.popapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class GameStatsDTO {
    private Integer timePlayedSeconds;
    private Integer scoredPoints;
    private String gameMode;
    private boolean wonGame;
}
