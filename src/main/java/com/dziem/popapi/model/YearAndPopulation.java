package com.dziem.popapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class YearAndPopulation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String yearOfMeasurement;
    private String population;
    private String annualGrowth;
    @ManyToOne
    @JoinColumn(name = "country_name", referencedColumnName = "countryName")
    @JsonBackReference
    private Country country;
    private Integer tier;
}
