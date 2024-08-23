package com.dziem.popapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Stats {
    @Id
    private String userId;
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
