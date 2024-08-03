package com.dziem.popapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

@Data
public class YearAndPopulationDTO {
    private Long id;
    private String yearOfMeasurment;
    private String population;
    private String annualGrowth;

    @JsonBackReference
    private Country country;
}
