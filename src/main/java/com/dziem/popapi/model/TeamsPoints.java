package com.dziem.popapi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity(name = "f1_teams_points")
@Data
public class TeamsPoints {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(precision = 10, scale = 2)
    private BigDecimal points;
    private String imageUrl;
    private String imageSource;
    private String imageSourceShort;
    private Integer tier;
}
