package com.dziem.popapi.dto.webpage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatsWithUNameDTO {
    private String userId;
    private Long totalGamePlayed;
    private BigDecimal avgScore;
    private String timePlayed;
    private Long totalScoredPoints;
    private Integer numberOfWonGames;
    private String name;
}
