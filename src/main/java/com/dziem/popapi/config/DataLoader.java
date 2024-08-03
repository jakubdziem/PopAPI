package com.dziem.popapi.config;

import com.dziem.popapi.model.CountryDTO;
import com.dziem.popapi.service.CountryService;
import com.dziem.popapi.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final DataService dataService;
    private final CountryService countryService;
    @Override
    public void run(String... args) throws Exception {
        dataService.getData2024_2100();
        dataService.getData2100_2150();
        List<CountryDTO> countriesByDistinctYear = countryService.findCountriesByDistinctYear("1989");
        System.out.println(countriesByDistinctYear.size());
    }
}