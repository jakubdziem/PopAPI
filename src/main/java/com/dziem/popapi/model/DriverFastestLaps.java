package com.dziem.popapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
@Entity(name = "drivers_fastest_laps")
@Data
public class DriverFastestLaps {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer laps;
    private String imageUrl;
    private String imageSource;
    private String imageSourceShort;
    private Integer tier;
}
