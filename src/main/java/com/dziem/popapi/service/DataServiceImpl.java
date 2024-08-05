package com.dziem.popapi.service;

import com.dziem.popapi.model.Country;
import com.dziem.popapi.model.YearAndPopulation;
import com.dziem.popapi.repository.CountryRepository;
import com.dziem.popapi.repository.YearAndPopulationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DataServiceImpl implements DataService {
    private final CountryRepository countryRepository;
    private final YearAndPopulationRepository yearAndPopulationRepository;
    @Override
    @Transactional
    public void getData2024_2100() {
        try (Stream<String> stream = Files.lines(Paths.get("src/main/resources/2024-2100 data.txt"))) {
            stream.skip(1).forEach(line -> {
                Country country = getCountry(line);
                countryRepository.save(country);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Country getCountry(String line) {
        String[] values = line.split(",");
        Country country = new Country();
        country.setCountryName(values[0].replace("\"", ""));
        if(country.getCountryName().equals("Korea")) {
            country.setCountryName(values[1].substring(1,6) + " Korea");
        }
        country.setGENC(values[values.length-5].replace("\"", ""));
        country.setFlagUrl("/images/" + country.getGENC() + ".png");
        YearAndPopulation yearAndPopulation = new YearAndPopulation();
        yearAndPopulation.setYearOfMeasurment(values[values.length-4].replace("\"", ""));
        yearAndPopulation.setPopulation(values[values.length-3].replace("\"", ""));
        yearAndPopulation.setAnnualGrowth(values[values.length-2].replace("\"", "")
                .concat(","+values[values.length-1].replace("\"", "")));
        yearAndPopulation.setCountry(country);
        country.getYearAndPopulations().add(yearAndPopulation);
        return country;
    }

    @Override
    public void getData2100_2150() {
        List<YearAndPopulation> list2100 = new ArrayList<>(yearAndPopulationRepository.findAll().stream().filter(y -> y.getYearOfMeasurment().equals("2100")).toList());
        String currentYear = "2100";
        for(int i = 1; i <= 50; i++) {
            currentYear = Integer.parseInt(currentYear) >= 2109 ? "21" + i : "210" + i;
            for(int j = 0; j < list2100.size(); j++) {
                YearAndPopulation yearAndPopulation = new YearAndPopulation();
                yearAndPopulation.setAnnualGrowth(list2100.get(j).getAnnualGrowth());
                yearAndPopulation.setYearOfMeasurment(currentYear);
                yearAndPopulation.setCountry(list2100.get(j).getCountry());

                double annualGrowth = list2100.get(j).getAnnualGrowth().indexOf(0) == '-' ?
                        -Double.parseDouble(list2100.get(j).getAnnualGrowth().substring(1)) :
                        Double.parseDouble(list2100.get(j).getAnnualGrowth().replace(',', '.'));
                BigDecimal population = new BigDecimal(list2100.get(j).getPopulation());
                BigDecimal populationCalculated = population.add(population.multiply(new BigDecimal(annualGrowth/100)));

                String populationInTheNextYear = String.valueOf(populationCalculated.intValue());
                list2100.get(j).setPopulation(populationInTheNextYear);
                list2100.set(j, list2100.get(j)); //updating
                yearAndPopulation.setPopulation(populationInTheNextYear);
                yearAndPopulationRepository.save(yearAndPopulation);
            }
        }
    }


}
