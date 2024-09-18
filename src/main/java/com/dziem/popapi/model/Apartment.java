package com.dziem.popapi.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="name")
    private String countryOrCityName;
    private Float price;
    private String category;
    private String imageUrl;
    private Integer tier;
    private String imageSource;
}
