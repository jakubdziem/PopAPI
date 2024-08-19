package com.dziem.popapi.controller;

import com.dziem.popapi.model.CountryDTO;
import com.dziem.popapi.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@RestController
@RequiredArgsConstructor
public class CountryController {
    private final CountryService countryService;
    @GetMapping("/api/v1/pop/{year}")
    public List<CountryDTO> getPopInDistinctYear(@PathVariable("year") String year) {
        return countryService.findCountriesByDistinctYear(year, false);
    }
    @GetMapping("/api/v1/pop/chaos/{chaos}")
    public List<CountryDTO> getPopInDistinctYearChaos(@PathVariable("chaos") boolean chaos) {
            List<CountryDTO> chaosList = new ArrayList<>();
            chaosList.addAll(countryService.findCountriesByDistinctYear("1900", chaos));
            chaosList.addAll(countryService.findCountriesByDistinctYear("1939", chaos));
            chaosList.addAll(countryService.findCountriesByDistinctYear("1989", chaos));
            int currentYear = LocalDate.now().getYear();
            chaosList.addAll(countryService.findCountriesByDistinctYear(String.valueOf(currentYear), chaos));
            chaosList.addAll(countryService.findCountriesByDistinctYear(String.valueOf(currentYear + 100), chaos));
        return chaosList;
    }
}
