package com.dziem.popapi.service;

import com.dziem.popapi.mapper.CountryMapper;
import com.dziem.popapi.model.Country;
import com.dziem.popapi.model.CountryDTO;
import com.dziem.popapi.model.YearAndPopulation;
import com.dziem.popapi.repository.CountryRepository;
import com.dziem.popapi.repository.YearAndPopulationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService{
    private final CountryRepository countryRepository;
    private final YearAndPopulationRepository yearAndPopulationRepository;
    private final CountryMapper countryMapper;
    @Override
    public List<CountryDTO> findCountriesByDistinctYear(String year) {
        List<Country> allCountries = countryRepository.findAll();
        List<YearAndPopulation> allYearsAndPopulations = yearAndPopulationRepository.findAll();
        int yearNum = Integer.parseInt(year);
        List<Country> countries;
        List<YearAndPopulation> yearAndPopulationList;
        switch(yearNum) {
            case 1900: {
                countries = allCountries.stream().filter(c -> c.getCountryName().endsWith("1900")).toList();
                yearAndPopulationList = allYearsAndPopulations.stream().filter(yearAndPopulation ->
                        yearAndPopulation.getCountry().getCountryName().endsWith("1900")).toList();
                break;
            }
            case 1939: {
                countries = allCountries.stream().filter(c -> c.getCountryName().endsWith("1939")).toList();
                yearAndPopulationList = allYearsAndPopulations.stream().filter(yearAndPopulation ->
                        yearAndPopulation.getCountry().getCountryName().endsWith("1939")).toList();

                break;
            }
            case 1989: {
                countries = allCountries.stream().filter(c -> c.getCountryName().endsWith("1989")).toList();
                yearAndPopulationList = allYearsAndPopulations.stream().filter(yearAndPopulation ->
                        yearAndPopulation.getCountry().getCountryName().endsWith("1989")).toList();
                break;
            }
            default: {
                countries = allCountries.stream().filter(c ->
                        (!c.getCountryName().endsWith("1989") &&
                        !c.getCountryName().endsWith("1900") &&
                        !c.getCountryName().endsWith("1939"))).toList();
                yearAndPopulationList = allYearsAndPopulations.stream().filter(yearAndPopulation ->
                        (!yearAndPopulation.getCountry().getCountryName().endsWith("1989") &&
                                !yearAndPopulation.getCountry().getCountryName().endsWith("1900") &&
                                !yearAndPopulation.getCountry().getCountryName().endsWith("1939"))
                                && yearAndPopulation.getYearOfMeasurment().equals(year)).toList();
                break;
            }
        }

        List<CountryDTO> countryDTOS = new ArrayList<>();
        for(int i = 0; i < countries.size(); i++) {
            List<YearAndPopulation> singleYearAndPopulation = new ArrayList<>();
            singleYearAndPopulation.add(yearAndPopulationList.get(i));

            countries.get(i).setYearAndPopulations(singleYearAndPopulation);

            CountryDTO countryDTO = countryMapper.countryToCountryDto(countries.get(i));
            countryDTOS.add(countryDTO);
        }
        return countryDTOS;
    }
}
