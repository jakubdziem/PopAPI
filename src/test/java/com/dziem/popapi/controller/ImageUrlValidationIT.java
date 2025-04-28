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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ImageUrlValidationIT {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private SongRepository songRepository;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private ApartmentRepository apartmentRepository;
    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private SocialMediaRepository socialMediaRepository;
    @Autowired
    private CinemaRepository cinemaRepository;
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
    @Test
    void allCountriesHaveValidFlagUrlUrlTest() throws Exception {
        List<Country> countries = countryRepository.findAll();
        for (Country country : countries) {
            String flagUrl = country.getFlagUrl();
            System.out.println("Testing flag URL: " + flagUrl);
            mockMvc.perform(get(flagUrl))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("image/png"));
        }
    }
    @Test
    void allArtistsHaveValidFlagUrlUrlTest() throws Exception {
        List<Artist> artists = artistRepository.findAll();
        for (Artist artist : artists) {
            String imageUrl = artist.getImageUrl();
            System.out.println("Testing image URL: " + imageUrl);
            mockMvc.perform(get(imageUrl))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("image/jpeg"));

        }
    }
    @Test
    void allSongsHaveValidFlagUrlUrlTest() throws Exception {
        List<Song> songs = songRepository.findAll();
        for (Song song : songs) {
            String imageUrl = song.getImageUrl();
            System.out.println("Testing image URL: " + imageUrl);
            mockMvc.perform(get(imageUrl))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("image/jpeg"));
        }
    }
    @Test
    void allApartmentsHaveValidFlagUrlUrlTest() throws Exception {
        List<Apartment> apartments = apartmentRepository.findAll();
        for (Apartment apartment : apartments) {
            String imageUrl = apartment.getImageUrl();
            System.out.println("Testing image URL: " + imageUrl);
            mockMvc.perform(get(imageUrl))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("image/png"));
        }
    }
    @Test
    void allHistoryEventsHaveValidFlagUrlUrlTest() throws Exception {
        List<History> historyEvents = historyRepository.findAll();
        for (History historyEvent : historyEvents) {
            String imageUrl = historyEvent.getImageUrl();
            System.out.println("Testing image URL: " + imageUrl);
            mockMvc.perform(get(imageUrl))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("image/png"));
        }
    }
    @Test
    void allSocialMediaHaveValidFlagUrlUrlTest() throws Exception {
        List<SocialMedia> socialMediaList = socialMediaRepository.findAll();
        for (SocialMedia socialMedia : socialMediaList) {
            String imageUrl = socialMedia.getImageUrl();
            System.out.println("Testing image URL: " + imageUrl);
            mockMvc.perform(get(imageUrl))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("image/png"));
        }
    }
    @Test
    void allCinemaHaveValidFlagUrlUrlTest() throws Exception {
        List<Cinema> cinemaList = cinemaRepository.findAll();
        for (Cinema cinema : cinemaList) {
            String imageUrl = cinema.getImageUrl();
            System.out.println("Testing image URL: " + imageUrl);
            mockMvc.perform(get(imageUrl))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("image/png"));
        }
    }
}
