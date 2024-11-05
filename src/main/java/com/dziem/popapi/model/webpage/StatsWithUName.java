package com.dziem.popapi.model.webpage;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@Builder
public class StatsWithUName {
    private String userId;
    private Long totalGamePlayed;
    private BigDecimal avgScore;
    private LocalTime timePlayed;
    private Long totalScoredPoints;
    private Integer numberOfWonGames;
    private String name;
}
