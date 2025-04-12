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
        String responseContent = mockMvc.perform(get("/api/v1/mode/" + Mode.COUNTRIES_1900))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<BaseGameModelDTO> baseGameModelDTOS = objectMapper.readValue(
                responseContent,
                new TypeReference<List<BaseGameModelDTO>>() {}
        );

        assertThat(baseGameModelDTOS.size()).isEqualTo(89);
    }
    @Test
    void shouldReturnElementsForCountries1939Mode() throws Exception {
        String responseContent = mockMvc.perform(get("/api/v1/mode/" + Mode.COUNTRIES_1939))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<BaseGameModelDTO> baseGameModelDTOS = objectMapper.readValue(
                responseContent,
                new TypeReference<List<BaseGameModelDTO>>() {}
        );

        assertThat(baseGameModelDTOS.size()).isEqualTo(70);
    }
    @Test
    void shouldReturnElementsForCountries1989Mode() throws Exception {
        String responseContent = mockMvc.perform(get("/api/v1/mode/" + Mode.COUNTRIES_1989))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<BaseGameModelDTO> baseGameModelDTOS = objectMapper.readValue(
                responseContent,
                new TypeReference<List<BaseGameModelDTO>>() {}
        );

        assertThat(baseGameModelDTOS.size()).isEqualTo(171);
    }
    @Test
    void shouldReturnElementsForCountriesNormalMode() throws Exception {
        String responseContent = mockMvc.perform(get("/api/v1/mode/" + Mode.COUNTRIES_NORMAL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<BaseGameModelDTO> baseGameModelDTOS = objectMapper.readValue(
                responseContent,
                new TypeReference<List<BaseGameModelDTO>>() {}
        );

        assertThat(baseGameModelDTOS.size()).isEqualTo(195);
    }
    @Test
    void shouldReturnElementsForCountriesFutureMode() throws Exception {
        String responseContent = mockMvc.perform(get("/api/v1/mode/" + Mode.COUNTRIES_FUTURE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<BaseGameModelDTO> baseGameModelDTOS = objectMapper.readValue(
                responseContent,
                new TypeReference<List<BaseGameModelDTO>>() {}
        );

        assertThat(baseGameModelDTOS.size()).isEqualTo(195);
    }
    @Test
    void shouldReturnElementsForCountriesChaosMode() throws Exception {
        String responseContent = mockMvc.perform(get("/api/v1/mode/" + Mode.COUNTRIES_CHAOS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<BaseGameModelDTO> baseGameModelDTOS = objectMapper.readValue(
                responseContent,
                new TypeReference<List<BaseGameModelDTO>>() {}
        );

        assertThat(baseGameModelDTOS.size()).isEqualTo(720);
    }
}
