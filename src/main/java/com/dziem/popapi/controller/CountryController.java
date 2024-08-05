package com.dziem.popapi.controller;

import com.dziem.popapi.model.CountryDTO;
import com.dziem.popapi.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequiredArgsConstructor
public class CountryController {
    private final CountryService countryService;
    @GetMapping("/api/v1/pop/{year}")
    public List<CountryDTO> getPopInDistinctYear(@PathVariable("year") String year) {
        return countryService.findCountriesByDistinctYear(year);
    }
}
