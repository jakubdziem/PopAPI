package com.dziem.popapi.controller;

import com.dziem.popapi.dto.webpage.DailyUsersSummedBothDTO;
import com.dziem.popapi.model.Mode;
import com.dziem.popapi.model.webpage.DailyActiveUsers;
import com.dziem.popapi.model.webpage.DailyStatsSummed;
import com.dziem.popapi.model.webpage.WeeklyActiveUsers;
import com.dziem.popapi.model.webpage.WeeklyNewUsersSummed;
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
public class StatsPageChartControllerIT {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void shouldReturnDailyStatsSummedPerMode_whenValidModeProvided() throws Exception {
        for(Mode mode : Mode.values()) {
            String responseContent = mockMvc.perform(get("/stats_for_chart/" + mode.toString()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            List<DailyStatsSummed> dailyStatsSummedList = objectMapper.readValue(
                    responseContent,
                    new TypeReference<List<DailyStatsSummed>>() {});
            assertThat(dailyStatsSummedList.size()).isGreaterThan(0);
            DailyStatsSummed first = dailyStatsSummedList.getFirst();
            assertThat(first.getId()).isNotNull();
            assertThat(first.getMode()).isNotNull();
            assertThat(first.getAvgScore()).isNotNull();
            assertThat(first.getDay()).isNotNull();
            assertThat(first.getTimePlayed()).isNotNull();
            assertThat(first.getNumberOfWonGames()).isNotNull();
            assertThat(first.getTotalGamePlayed()).isNotNull();
            assertThat(first.getTotalScoredPoints()).isNotNull();
        }
    }
    @Test
    void shouldReturnEmptyList_whenInvalidModeProvided() throws Exception {
        String responseContent = mockMvc.perform(get("/stats_for_chart/" + "INVALID_MODE"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
        List<DailyStatsSummed> dailyStatsSummedList = objectMapper.readValue(
                    responseContent,
                    new TypeReference<List<DailyStatsSummed>>() {});
        assertThat(dailyStatsSummedList).isEmpty();
    }
    @Test
    void shouldReturnSummedDailyNewUsers_whenRequested() throws Exception {
        String responseContent = mockMvc.perform(get("/stats_for_chart_new_users"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andReturn()
               .getResponse()
               .getContentAsString();
        List<DailyUsersSummedBothDTO> dailyUsersSummedBothDTOS = objectMapper.readValue(
                responseContent,
                new TypeReference<List<DailyUsersSummedBothDTO>>() {});
        assertThat(dailyUsersSummedBothDTOS.size()).isGreaterThan(0);
        DailyUsersSummedBothDTO first = dailyUsersSummedBothDTOS.getFirst();
        DailyUsersSummedBothDTO second = dailyUsersSummedBothDTOS.get(1);
        assertThat(first.getDay()).isNotNull();
        assertThat(first.getNumberOfUsers()).isGreaterThanOrEqualTo(0);
        assertThat(first.getDay().plusDays(1)).isEqualTo(second.getDay());
    }
    @Test
    void shouldReturnWeeklyNewUsersSummed_whenRequested() throws Exception {
        String responseContent = mockMvc.perform(get("/stats_for_chart_new_users_weekly"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<WeeklyNewUsersSummed> weeklyNewUsersSummedList = objectMapper.readValue(
                responseContent,
                new TypeReference<List<WeeklyNewUsersSummed>>() {});

        assertThat(weeklyNewUsersSummedList.size()).isGreaterThan(0);
        WeeklyNewUsersSummed first = weeklyNewUsersSummedList.getFirst();
        WeeklyNewUsersSummed second = weeklyNewUsersSummedList.get(1);
        assertThat(first.getWeekStartDate()).isNotNull();
        assertThat(first.getNewUsers()).isGreaterThanOrEqualTo(0);
        assertThat(first.getWeekStartDate().plusWeeks(1)).isEqualTo(second.getWeekStartDate());
    }
    @Test
    void shouldReturnDailyActiveUsers_whenRequested() throws Exception {
        String responseContent = mockMvc.perform(get("/stats_for_chart_active_users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<DailyActiveUsers> dailyActiveUsers = objectMapper.readValue(
                responseContent,
                new TypeReference<List<DailyActiveUsers>>() {});

        assertThat(dailyActiveUsers.size()).isGreaterThan(0);
        DailyActiveUsers first = dailyActiveUsers.getFirst();
        DailyActiveUsers second = dailyActiveUsers.get(1);
        assertThat(first.getDay()).isNotNull();
        assertThat(first.getActiveUsers()).isGreaterThanOrEqualTo(0);
        assertThat(first.getDay().plusDays(1)).isEqualTo(second.getDay());
    }
    @Test
    void shouldReturnWeeklyActiveUsers_whenRequested() throws Exception {
        String responseContent = mockMvc.perform(get("/stats_for_chart_active_users_weekly"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<WeeklyActiveUsers> weeklyActiveUsers = objectMapper.readValue(
                responseContent,
                new TypeReference<List<WeeklyActiveUsers>>() {});

        assertThat(weeklyActiveUsers.size()).isGreaterThan(0);
        WeeklyActiveUsers first = weeklyActiveUsers.getFirst();
        WeeklyActiveUsers second = weeklyActiveUsers.get(1);
        assertThat(first.getWeekStartDate()).isNotNull();
        assertThat(first.getActiveNewUsers()).isGreaterThanOrEqualTo(0);
        assertThat(first.getActiveOldUsers()).isGreaterThanOrEqualTo(0);
        assertThat(first.getActiveUsers()).isEqualTo(first.getActiveNewUsers() + first.getActiveOldUsers());
        assertThat(first.getWeekStartDate().plusWeeks(1)).isEqualTo(second.getWeekStartDate());
    }
}
