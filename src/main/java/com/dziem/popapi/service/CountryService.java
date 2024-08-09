package com.dziem.popapi.service;

import com.dziem.popapi.model.CountryDTO;

import java.util.List;

public interface CountryService {
    List<CountryDTO> findCountriesByDistinctYear(String year, boolean chaos);

}
