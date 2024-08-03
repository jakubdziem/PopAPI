package com.dziem.popapi.model;

import com.dziem.popapi.model.YearAndPopulation;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import java.util.List;

@Data
public class CountryDTO {
    private String countryName;
    @JsonManagedReference
    private List<YearAndPopulation> yearAndPopulations;
    private String GENC;

}
