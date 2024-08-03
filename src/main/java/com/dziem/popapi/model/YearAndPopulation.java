package com.dziem.popapi.model;

import com.dziem.popapi.model.Country;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class YearAndPopulation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String yearOfMeasurment;
    private String population;
    private String annualGrowth;
    @ManyToOne
    @JoinColumn(name = "country_name", referencedColumnName = "countryName")
    private Country country;
}
