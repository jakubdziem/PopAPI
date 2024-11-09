package com.dziem.popapi.model.webpage;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Builder
@RequiredArgsConstructor
public class WeeklyStats {
    @Id
    private LocalDate weekStartDate;
    @Id
    private String userId;
    private String mode;
    private Long totalGamePlayed;
    private BigDecimal avgScore;
    private String timePlayed;
    private Long totalScoredPoints;
    private Integer numberOfWonGames;
    private String name;
}
