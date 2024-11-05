package com.dziem.popapi.model.webpage;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class StatsWithUName {
    private String userId;
    private Long totalGamePlayed;
    private BigDecimal avgScore;
    private String timePlayed;
    private Long totalScoredPoints;
    private Integer numberOfWonGames;
    private String name;
}
