package com.dziem.popapi.dto;

import com.dziem.popapi.model.YearAndPopulation;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CountryDTO {
    private String countryName;
    private List<YearAndPopulation> yearAndPopulations;
    private String GENC;
    private String flagUrl;
    private String imageSource;
    private String imageSourceShort;
}
