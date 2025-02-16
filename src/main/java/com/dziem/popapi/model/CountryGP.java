package com.dziem.popapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity(name = "countries_gp")
@Data
public class CountryGP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer gp;
    private String imageUrl;
    private String imageSource;
    private String imageSourceShort;
    private Integer tier;
}
