package com.dziem.popapi.model;

import lombok.Data;

import java.util.List;

@Data
public class CountryDTO {
    private String countryName;
    private List<YearAndPopulation> yearAndPopulations;
    private String GENC;

}
