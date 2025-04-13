package com.dziem.popapi.controller;

import com.dziem.popapi.model.BaseGameModelDTO;
import com.dziem.popapi.model.Mode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class ModeControllerIT {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    @Test
    void shouldReturnElementsForCountries1900Mode() throws Exception {
        assertThatRequestedModeHasSize(Mode.COUNTRIES_1900, 89);
    }
    @Test
    void shouldReturnElementsForCountries1939Mode() throws Exception {
        assertThatRequestedModeHasSize(Mode.COUNTRIES_1939, 70);
    }
    @Test
    void shouldReturnElementsForCountries1989Mode() throws Exception {
        assertThatRequestedModeHasSize(Mode.COUNTRIES_1989, 171);
    }
    @Test
    void shouldReturnElementsForCountriesNormalMode() throws Exception {
        assertThatRequestedModeHasSize(Mode.COUNTRIES_NORMAL, 195);
    }
    @Test
    void shouldReturnElementsForCountriesFutureMode() throws Exception {
        assertThatRequestedModeHasSize(Mode.COUNTRIES_FUTURE, 195);
    }
    @Test
    void shouldReturnElementsForCountriesChaosMode() throws Exception {
        assertThatRequestedModeHasSize(Mode.COUNTRIES_CHAOS, 720);
    }
    @Test
    void shouldReturnElementsForSpotifyTopArtistsMode() throws Exception {
        assertThatRequestedModeHasSize(Mode.SPOTIFY_TOP_ARTISTS, 200);
    }
    @Test
    void shouldReturnElementsForSpotifyTopSongsGeneralMode() throws Exception {
        assertThatRequestedModeHasSize(Mode.SPOTIFY_TOP_SONGS_GENERAL, 200);
    }
    @Test
    void shouldReturnElementsForSpotifyTopSongsPopMode() throws Exception {
        assertThatRequestedModeHasSize(Mode.SPOTIFY_TOP_SONGS_POP, 200);
    }
    @Test
    void shouldReturnElementsForSpotifyTopSongsHipHopMode() throws Exception {
        assertThatRequestedModeHasSize(Mode.SPOTIFY_TOP_SONGS_HIP_HOP, 200);
    }
    @Test
    void shouldReturnElementsForSpotifyTopSongsRockMode() throws Exception {
        assertThatRequestedModeHasSize(Mode.SPOTIFY_TOP_SONGS_ROCK, 200);
    }
    @Test
    void shouldReturnElementsForFormulaTopScoreMode() throws Exception {
        assertThatRequestedModeHasSize(Mode.FORMULA_TOP_SCORE, 100);
    }
    @Test
    void shouldReturnElementsForFormulaTopDriversPodiumsMode() throws Exception {
        assertThatRequestedModeHasSize(Mode.FORMULA_TOP_DRIVERS_PODIUMS, 100);
    }
    @Test
    void shouldReturnElementsForFormulaTopDriversFastestLapsMode() throws Exception {
        assertThatRequestedModeHasSize(Mode.FORMULA_TOP_DRIVERS_FASTEST_LAPS, 50);
    }
    @Test
    void shouldReturnElementsForFormulaTopDriversGPMode() throws Exception {
        assertThatRequestedModeHasSize(Mode.FORMULA_TOP_DRIVERS_GP, 100);
    }
    @Test
    void shouldReturnElementsForFormulaTopCountriesGPMode() throws Exception {
        assertThatRequestedModeHasSize(Mode.FORMULA_TOP_COUNTRIES_GP, 39);
    }
    @Test
    void shouldReturnElementsForFormulaTopTeamsPointsMode() throws Exception {
        assertThatRequestedModeHasSize(Mode.FORMULA_TOP_TEAMS_POINTS, 50);
    }
    @Test
    void shouldReturnElementsForFormulaTopTeamsGPMode() throws Exception {
        assertThatRequestedModeHasSize(Mode.FORMULA_TOP_TEAMS_GP, 50);
    }

    private void assertThatRequestedModeHasSize(Mode mode, int size) throws Exception {
        String responseContent = mockMvc.perform(get("/api/v1/mode/" + mode))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<BaseGameModelDTO> baseGameModelDTOS = objectMapper.readValue(
                responseContent,
                new TypeReference<List<BaseGameModelDTO>>() {}
        );

        assertThat(baseGameModelDTOS.size()).isEqualTo(size);
    }

    @Test
    void shouldReturnElementsForApartmentsWorldMode() throws Exception {
        assertThatRequestedModeHasSize(Mode.APARTMENTS_WORLD, 96);
    }
    @Test
    void shouldReturnElementsForApartmentsPolandMode() throws Exception {
        assertThatRequestedModeHasSize(Mode.APARTMENTS_POLAND, 65);
    }
    @Test
    void shouldReturnElementsForHistoryMode() throws Exception {
        assertThatRequestedModeHasSize(Mode.HISTORY, 124);
    }
    @Test
    void shouldReturnElementsForSocialMediaTwitterMode() throws Exception {
        assertThatRequestedModeHasSize(Mode.SOCIAL_MEDIA_TWITTER, 50);
    }
    @Test
    void shouldReturnElementsForSocialMediaInstagramMode() throws Exception {
        assertThatRequestedModeHasSize(Mode.SOCIAL_MEDIA_INSTAGRAM, 50);
    }
    @Test
    void shouldReturnElementsForSocialMediaTikTokMode() throws Exception {
        assertThatRequestedModeHasSize(Mode.SOCIAL_MEDIA_TIK_TOK, 50);
    }
    @Test
    void shouldReturnElementsForSocialMediaYouTubeMode() throws Exception {
        assertThatRequestedModeHasSize(Mode.SOCIAL_MEDIA_YOUTUBE, 50);
    }
    @Test
    void shouldReturnElementsForMoviesMode() throws Exception {
        assertThatRequestedModeHasSize(Mode.MOVIES, 150);
    }
    @Test
    void shouldReturnElementsForTVShowsMode() throws Exception {
        assertThatRequestedModeHasSize(Mode.TV_SHOWS, 150);
    }
    @Test
    void shouldReturnElementsForCelebsMode() throws Exception {
        assertThatRequestedModeHasSize(Mode.CELEBS, 100);
    }
}
