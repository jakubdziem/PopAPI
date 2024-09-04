package com.dziem.popapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ModeStatsDTO {
    @JsonIgnore
    private Long id;
    @JsonIgnore
    private User user;
    private Long totalGamePlayed;
    private BigDecimal avgScore;
    private Long timePlayed;
    private Long totalScoredPoints;
    private Integer numberOfWonGames;
    private String mode;
    private Integer bestScore;
}
