package com.dziem.popapi.controller;

import com.dziem.popapi.model.*;
import com.dziem.popapi.repository.LeaderboardRepository;
import com.dziem.popapi.repository.UserRepository;
import com.dziem.popapi.service.LeaderboardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class LeaderboardControllerITTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private LeaderboardService leaderboardService;
    @Autowired
    private LeaderboardRepository leaderboardRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    private MockMvc mockMvc;
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    @Test
    void shouldReturnRankWhenUserHasRankInMode() throws Exception {
        Mode mode = Mode.COUNTRIES_1900;
        Leaderboard leaderboard = leaderboardRepository.findAll().stream().filter(
                l -> l.getMode().equals(mode.toString())).toList().get(0);
        User user = leaderboard.getUser();
        String userId = user.getUserId();

        String responseContent = mockMvc.perform(get("/api/v1/rank/" + userId + "/" + mode))
                .andExpect(status().isAccepted())
                .andReturn()
                .getResponse()
                .getContentAsString();

        int rank = 0;
        List<LeaderboardDTO> leaderboards = leaderboardService.getLeaderboard(mode.toString());

        for (int i = 0; i < (leaderboards != null ? leaderboards.size() : 0); i++) {
            if (leaderboards.get(i).getUserId().equals(userId)) {
                rank = i + 1;
                break;
            }
        }

        RankScoreDTO rankScoreDTO = objectMapper.readValue(responseContent, RankScoreDTO.class);
        System.out.println(rankScoreDTO.toString());
        assertThat(rankScoreDTO.getRank()).isEqualTo(rank);
        assertThat(rankScoreDTO.getScore()).isEqualTo(leaderboard.getScore());
    }
    @Test
    void shouldReturnNotFoundWhenUserHasNoRankInMode() throws Exception {
        Mode mode = Mode.COUNTRIES_1900;

        String incorrectUserId = "incorrect_user_id";

        assertThat(userRepository.findById(incorrectUserId)).isEmpty();

        mockMvc.perform(get("/api/v1/rank/" + incorrectUserId + "/" + mode))
                .andExpect(status().isNotFound());
    }
    @Test
    void shouldReturnNotFoundWhenLeaderboardModeDoesNotExist() throws Exception {
        Mode mode = Mode.COUNTRIES_1900;
        String incorrectMode = "OUNTRIES_1900";
        Leaderboard leaderboard = leaderboardRepository.findAll().stream().filter(
                l -> l.getMode().equals(mode.toString())).toList().get(0);
        User user = leaderboard.getUser();
        String userId = user.getUserId();

        mockMvc.perform(get("/api/v1/rank/" + userId + "/" + incorrectMode))
                .andExpect(status().isNotFound());
    }
}
