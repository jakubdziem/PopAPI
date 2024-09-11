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
    public List<CountryDTO> findCountriesByDistinctYear(String year, boolean chaos) {
        List<Country> allCountries = countryRepository.findAll();
        List<YearAndPopulation> allYearsAndPopulations = yearAndPopulationRepository.findAll();
        int yearNum;
        try {
            yearNum = Integer.parseInt(year);
        } catch (NumberFormatException e) {
            ArrayList<CountryDTO> emptyList = new ArrayList<>();
            emptyList.add(CountryDTO.builder().countryName("This is not a year, try something in this format: /api/v1/pop/2024").build());
            return emptyList;
        }
        List<Country> countries = switchForCountries(yearNum, allCountries);
        List<YearAndPopulation> yearAndPopulationList = switchForYearsAndPopulation(yearNum, allYearsAndPopulations);
        if(countries.isEmpty()) {
            ArrayList<CountryDTO> emptyList = new ArrayList<>();
            emptyList.add(CountryDTO.builder().countryName("The " + yearNum + " is not in the database.")
                    .flagUrl(" Try one of these: /api/v1/pop/1900, /api/v1/pop/1939, /api/v1/pop/1989, or from the 2024-2150 range").build());
            return emptyList;
        }
        return getCountryDTOS(countries, yearAndPopulationList, chaos);
    }


    private List<CountryDTO> getCountryDTOS(List<Country> countries, List<YearAndPopulation> yearAndPopulationList, boolean chaos) {
        List<CountryDTO> countryDTOS = new ArrayList<>();
        for (int i = 0; i < countries.size(); i++) {
            List<YearAndPopulation> singleYearAndPopulation = new ArrayList<>();
            int index = i;
            singleYearAndPopulation.add(yearAndPopulationList.stream().filter(c -> c.getCountry().getCountryName().equals(countries.get(index).getCountryName())).toList().get(0));

            countries.get(i).setYearAndPopulations(singleYearAndPopulation);

            CountryDTO countryDTO = countryMapper.countryToCountryDto(countries.get(i));
            countryDTO.setFlagUrl(countryDTO.getFlagUrl().toLowerCase());
            String countryName = countryDTO.getCountryName();
            boolean haveYearInName = countryName.endsWith("1900") || countryName.endsWith("1939") || countryName.endsWith("1989");
            if(chaos) {
                countryDTO.setCountryName(haveYearInName
                        ? countryName : countryName + " " + singleYearAndPopulation.get(0).getYearOfMeasurement());
            } else {
                countryDTO.setCountryName(haveYearInName
                        ? countryName.substring(0, countryName.length() - 5) : countryName);
            }
            countryDTOS.add(countryDTO);
        }
        return countryDTOS;
    }

    private List<YearAndPopulation> switchForYearsAndPopulation(int yearNum, List<YearAndPopulation> allYearsAndPopulations) {
        List<YearAndPopulation> yearAndPopulationList;
        switch(yearNum) {
            case 1900: {
                yearAndPopulationList = allYearsAndPopulations.stream().filter(yearAndPopulation ->
                        yearAndPopulation.getCountry().getCountryName().endsWith("1900")).toList();
                break;
            }
            case 1939: {
                yearAndPopulationList = allYearsAndPopulations.stream().filter(yearAndPopulation ->
                        yearAndPopulation.getCountry().getCountryName().endsWith("1939")).toList();

                break;
            }
            case 1989: {
                yearAndPopulationList = allYearsAndPopulations.stream().filter(yearAndPopulation ->
                        yearAndPopulation.getCountry().getCountryName().endsWith("1989")).toList();
                break;
            }
            default: {
                if(yearNum >= 2023 && yearNum <= 2150) {
                    yearAndPopulationList = allYearsAndPopulations.stream().filter(yearAndPopulation ->
                            (!yearAndPopulation.getCountry().getCountryName().endsWith("1989") &&
                                    !yearAndPopulation.getCountry().getCountryName().endsWith("1900") &&
                                    !yearAndPopulation.getCountry().getCountryName().endsWith("1939"))
                                    && yearAndPopulation.getYearOfMeasurement().equals(String.valueOf(yearNum))).toList();
//                    for(YearAndPopulation yearAndPopulation : yearAndPopulationList) {
//                        System.out.println(yearAndPopulation);
//                    }
                } else {
                    return new ArrayList<>();
                }
                break;
            }
        }
        return yearAndPopulationList;
    }

    private List<Country> switchForCountries(int yearNum, List<Country> allCountries) {
        List<Country> countries;
        switch(yearNum) {
            case 1900: {
                countries = allCountries.stream().filter(c -> c.getCountryName().endsWith("1900")).toList();
                break;
            }
            case 1939: {
                countries = allCountries.stream().filter(c -> c.getCountryName().endsWith("1939")).toList();
                break;
            }
            case 1989: {
                countries = allCountries.stream().filter(c -> c.getCountryName().endsWith("1989")).toList();
                break;
            }
            default: {
                if(yearNum >= 2023 && yearNum <= 2150) {
                    countries = allCountries.stream().filter(c ->
                            (!c.getCountryName().endsWith("1989") &&
                                    !c.getCountryName().endsWith("1900") &&
                                    !c.getCountryName().endsWith("1939"))).toList();
                } else {
                    return new ArrayList<>();
                }
                break;
            }
        }
        return countries;
    }
}
