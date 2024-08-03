package com.dziem.popapi.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Entity
@Data
public class Country {
    @Id
    private String countryName;
    private String GENC;
    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL)
    private List<YearAndPopulation> yearAndPopulations;
    public Country() {
        yearAndPopulations = new ArrayList<>();
    }
}
