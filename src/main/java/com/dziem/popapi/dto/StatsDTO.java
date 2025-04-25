package com.dziem.popapi.dto;

import com.dziem.popapi.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class StatsDTO {
    @JsonIgnore
    private String userId;
    @JsonIgnore
    private User user;
    private Long totalGamePlayed;
    private BigDecimal avgScore;
    private Long timePlayed;
    private Long totalScoredPoints;
    private Integer numberOfWonGames;
}
