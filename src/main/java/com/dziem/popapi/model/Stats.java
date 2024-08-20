package com.dziem.popapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
public class Stats {
    @Id
    private UUID userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;
    private Long totalGamePlayed;
    private BigDecimal avgScore;
    private Long timePlayed;
    private Long totalScoredPoints;
    private Integer numberOfWonGames;
}
