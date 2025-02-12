package com.dziem.popapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity(name = "top_drivers_podiums")
@Data
public class DriverPodiums {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer podiums;
    private String imageUrl;
    private String imageSource;
    private String imageSourceShort;
    private Integer tier;
}
