package com.dziem.popapi.dto.webpage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
@AllArgsConstructor
public class ActiveUsersStatsDTO {
    private String userId;
    private Long totalGamePlayed;
    private BigDecimal avgScore;
    private String timePlayed;
    private Long totalScoredPoints;
    private Integer numberOfWonGames;
    private String name;
    private boolean newUser;
}
