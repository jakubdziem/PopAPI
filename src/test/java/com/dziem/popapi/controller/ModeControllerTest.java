package com.dziem.popapi.controller;

import com.dziem.popapi.model.BaseGameModelDTO;
import com.dziem.popapi.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ModeController.class)
@ActiveProfiles("test")
public class ModeControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private ModeStatsService modeStatsService;

    @BeforeEach
    void setUp() {
        BaseGameModelDTO driver = BaseGameModelDTO.builder()
                .name("Patrese Riccardo")
                .comparableValue(281.0f)
                .comparableValueLabel("points")
                .imageUrl("/images/f1/Patrese_Riccardo.png")
                .imageSource("Autosport")
                .tier(2)
                .build();
        BaseGameModelDTO country = BaseGameModelDTO.builder()
                .name("Poland")
                .comparableValue(400000000f)
                .comparableValueLabel("population")
                .imageUrl("/images/pl.png")
                .imageSource("Wikipedia")
                .tier(1)
                .build();
        when(modeStatsService.convertFormulaToBaseGameModelDTO()).thenReturn(List.of(driver));
        when(modeStatsService.convertCountryDTOtoBaseGameModelDTO("1900", false)).thenReturn(List.of(country));
    }
    @Test
    void shouldReturnValidFormulaTopScoreList() throws Exception {
        mockMvc.perform(get("/api/v1/mode/FORMULA_TOP_SCORE")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Patrese Riccardo"))
                .andExpect(jsonPath("$[0].comparableValue").value(281.0))
                .andExpect(jsonPath("$[0].comparableValueLabel").value("points"))
                .andExpect(jsonPath("$[0].imageUrl").value("/images/f1/Patrese_Riccardo.png"))
                .andExpect(jsonPath("$[0].imageSource").value("Autosport"))
                .andExpect(jsonPath("$[0].tier").value(2));
    }
    @Test
    void shouldReturnValidCountries1900List() throws Exception {
        mockMvc.perform(get("/api/v1/mode/COUNTRIES_1900")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Poland"))
                .andExpect(jsonPath("$[0].comparableValue").value(400000000f))
                .andExpect(jsonPath("$[0].comparableValueLabel").value("population"))
                .andExpect(jsonPath("$[0].imageUrl").value("/images/pl.png"))
                .andExpect(jsonPath("$[0].imageSource").value("Wikipedia"))
                .andExpect(jsonPath("$[0].tier").value(1));
    }
}

