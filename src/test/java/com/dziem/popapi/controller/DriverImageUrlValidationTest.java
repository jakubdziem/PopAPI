package com.dziem.popapi.controller;

import com.dziem.popapi.model.*;
import com.dziem.popapi.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class DriverImageUrlValidationTest {

    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private DriverPodiumsRepository driverPodiumsRepository;
    @Autowired
    private DriverGPRepository driverGPRepository;
    @Autowired
    private DriverFastestLapsRepository driverFastestLapsRepository;
    @Autowired
    private CountryGPRepository countryGPRepository;
    @Autowired
    private TeamsPointsRepository teamsPointsRepository;
    @Autowired
    private TeamsGPRepository teamsGPRepository;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void allDriversHaveValidImageUrlTest() throws Exception {
        List<Driver> drivers = driverRepository.findAll();
        for (Driver driver : drivers) {
            String imageUrl = driver.getImageUrl();
            System.out.println("Testing image URL: " + imageUrl);
            mockMvc.perform(get(imageUrl))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("image/png"));
        }
    }
    @Test
    void allDriversPodiumsHaveValidImageUrlTest() throws Exception {
        List<DriverPodiums> drivers = driverPodiumsRepository.findAll();
        for (DriverPodiums driver : drivers) {
            String imageUrl = driver.getImageUrl();
            System.out.println("Testing image URL: " + imageUrl);
            mockMvc.perform(get(imageUrl))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("image/png"));
        }
    }
    @Test
    void allDriversGPHaveValidImageUrlTest() throws Exception {
        List<DriverGP> drivers = driverGPRepository.findAll();
        for (DriverGP driver : drivers) {
            String imageUrl = driver.getImageUrl();
            System.out.println("Testing image URL: " + imageUrl);
            mockMvc.perform(get(imageUrl))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("image/png"));
        }
    }
    @Test
    void allDriversFastestLapsHaveValidImageUrlTest() throws Exception {
        List<DriverFastestLaps> drivers = driverFastestLapsRepository.findAll();
        for (DriverFastestLaps driver : drivers) {
            String imageUrl = driver.getImageUrl();
            System.out.println("Testing image URL: " + imageUrl);
            mockMvc.perform(get(imageUrl))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("image/png"));
        }
    }
    @Test
    void allCountriesGPHaveValidImageUrlTest() throws Exception {
        List<CountryGP> countries = countryGPRepository.findAll();
        for (CountryGP country : countries) {
            String imageUrl = country.getImageUrl();
            System.out.println("Testing image URL: " + imageUrl);
            mockMvc.perform(get(imageUrl))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("image/png"));
        }
    }
    @Test
    void allTeamsPointsHaveValidImageUrlTest() throws Exception {
        List<TeamsPoints> teams = teamsPointsRepository.findAll();
        for (TeamsPoints team : teams) {
            String imageUrl = team.getImageUrl();
            System.out.println("Testing image URL: " + imageUrl);
            mockMvc.perform(get(imageUrl))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("image/png"));
        }
    }
    @Test
    void allTeamsGPHaveValidImageUrlTest() throws Exception {
        List<TeamsGP> teams = teamsGPRepository.findAll();
        for (TeamsGP team : teams) {
            String imageUrl = team.getImageUrl();
            System.out.println("Testing image URL: " + imageUrl);
            mockMvc.perform(get(imageUrl))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("image/png"));
        }
    }
}
