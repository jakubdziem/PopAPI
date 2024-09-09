package com.dziem.popapi.model;

import lombok.Data;

@Data
public class YearAndPopulationDTO {
    private Long id;
    private String yearOfMeasurement;
    private String population;
    private String annualGrowth;
    private Country country;
    private Integer tier;
}
