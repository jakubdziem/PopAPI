package com.dziem.popapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
@Entity
@Data
public class ModeStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @JsonBackReference
    private User user;
    private Long totalGamePlayed;
    private BigDecimal avgScore;
    private Long timePlayed;
    private Long totalScoredPoints;
    private Integer numberOfWonGames;
    private String mode;
}
