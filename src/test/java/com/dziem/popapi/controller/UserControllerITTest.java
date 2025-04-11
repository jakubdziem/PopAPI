package com.dziem.popapi.controller;

import com.dziem.popapi.mapper.ModeStatsMapper;
import com.dziem.popapi.mapper.StatsMapper;
import com.dziem.popapi.model.*;
import com.dziem.popapi.repository.*;
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


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("dev")
public class UserControllerITTest {
    @Autowired
    UserController userController;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UNameRepository uNameRepository;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private StatsRepository statsRepository;
    @Autowired
    private ScoreRepository scoreRepository;
    @Autowired
    private ModeStatsRepository modeStatsRepository;
    @Autowired
    private StatsMapper statsMapper;
    @Autowired
    private ModeStatsMapper modeStatsMapper;
    private MockMvc mockMvc;
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        userRepository.deleteAll();
    }
    @Test
    void shouldCreateAnonimUserId() throws Exception {
        String responseContent  = mockMvc.perform(get("/api/v1/anonim_user_id"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").isString())
                .andExpect(jsonPath("$.userId").isNotEmpty())
                .andExpect(jsonPath("$.name").isString())
                .andExpect(jsonPath("$.name").isNotEmpty())
                .andReturn()
                .getResponse()
                .getContentAsString();
        UserDTO userDTO = objectMapper.readValue(responseContent, UserDTO.class);


        assertThat(userRepository.findById(userDTO.getUserId())).isPresent();
    }
    @Test
    void shouldMigrateAnonimUserToGoogleTest() throws Exception {
        String responseContent  = mockMvc.perform(get("/api/v1/anonim_user_id"))
                .andReturn().getResponse().getContentAsString();
        UserDTO userDTO = objectMapper.readValue(responseContent, UserDTO.class);
        User anonimUser = userRepository.findById(userDTO.getUserId()).get();
        String googleId = "TESTING";
        String anonimUserId = anonimUser.getUserId();
        String nickName = mockMvc.perform(put("/api/v1/google/" + anonimUserId + "/" + googleId))
                .andExpect(status().isAccepted())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andReturn()
                .getResponse()
                .getContentAsString();


        assertThat(nickName).isNotEmpty();
        System.out.println(nickName);
        assertThat(uNameRepository.findById(googleId).get().getName()).isEqualTo(nickName);
        assertThat(userRepository.findById(anonimUserId)).isNotPresent();
        assertThat(userRepository.findById(googleId)).isPresent();
    }
    @Test
    void shouldCreateGoogleUser() throws Exception {
        String googleId = "TESTING";
        String nickName  = mockMvc.perform(post("/api/v1/google/" + googleId))
                .andExpect(status().isAccepted())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andReturn()
                .getResponse()
                .getContentAsString();


        assertThat(nickName).isNotEmpty();
        System.out.println(nickName);
        assertThat(uNameRepository.findById(googleId).get().getName()).isEqualTo(nickName);
        assertThat(userRepository.findById(googleId)).isPresent();
    }
    @Test
    void shouldReturnConflictWhenCreatingGoogleUserThatAlreadyExists() throws Exception {
        String googleId = createUser();

        assertThat(userRepository.findById(googleId)).isPresent();

        mockMvc.perform(post("/api/v1/google/" + googleId))
                .andExpect(status().isConflict());
    }
    @Test
    void shouldReturnConflictWhenMigratingToGoogleUserThatAlreadyExists() throws Exception {
        String responseContent  = mockMvc.perform(get("/api/v1/anonim_user_id"))
                .andReturn().getResponse().getContentAsString();
        UserDTO userDTO = objectMapper.readValue(responseContent, UserDTO.class);
        User anonimUser = userRepository.findById(userDTO.getUserId()).get();
        String anonimUserId = anonimUser.getUserId();
        String googleId = "TESTING";

        mockMvc.perform(put("/api/v1/google/" + anonimUserId + "/" + googleId));


        String responseContent2  = mockMvc.perform(get("/api/v1/anonim_user_id"))
                .andReturn().getResponse().getContentAsString();
        UserDTO userDTO2 = objectMapper.readValue(responseContent2, UserDTO.class);
        User anonimUser2 = userRepository.findById(userDTO2.getUserId()).get();
        String anonimUserId2 = anonimUser2.getUserId();


        mockMvc.perform(put("/api/v1/google/" + anonimUserId2 + "/" + googleId))
                .andExpect(status().isConflict());
    }
    @Test
    void shouldReturnNotFoundWhenMigratingToGoogleUserWithAnonimUserThatDoesntExist() throws Exception {
        String anonimUserId = "anonim_user_id_that_doesnt_exist";
        String googleId = "TESTING";

        Optional<User> anonimUser = userRepository.findById(anonimUserId);

        assertThat(anonimUser).isEmpty();

        mockMvc.perform(put("/api/v1/google/" + anonimUserId + "/" + googleId))
            .andExpect(status().isNotFound());
    }
    @Test
    void shouldSetUsernameForGoogleUser() throws Exception {
        String googleId = "TESTING";
        String username = "good_morning";

        mockMvc.perform(post("/api/v1/google/" + googleId));

        String prevUsername = uNameRepository.findById(googleId).get().getName();

        mockMvc.perform(put("/api/v1/set_name/" + googleId + "/" + username))
                .andExpect(status().isNoContent());

        String currentUsername = uNameRepository.findById(googleId).get().getName();

        assertThat(username).isEqualTo(currentUsername);
        assertThat(prevUsername).isNotEqualTo(currentUsername);
    }
    @Test
    void shouldReturnBadRequestWhenSettingUsernameWithRestrictedWordForGoogleUser() throws Exception {
        String googleId = "TESTING";
        String username = "AdMin123";

        mockMvc.perform(post("/api/v1/google/" + googleId));

        String prevUsername = uNameRepository.findById(googleId).get().getName();

        mockMvc.perform(put("/api/v1/set_name/" + googleId + "/" + username))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Contained restricted word."));

        String currentUsername = uNameRepository.findById(googleId).get().getName();

        assertThat(prevUsername).isEqualTo(currentUsername);
        assertThat(username).isNotEqualTo(currentUsername);
    }
    @Test
    void shouldReturnConflictWhenChangingUsernameTooSoonForGoogleUser() throws Exception {
        String googleId = "TESTING";
        String username = "good_morning";
        String secondUsername = "good_evening";

        mockMvc.perform(post("/api/v1/google/" + googleId));

        mockMvc.perform(put("/api/v1/set_name/" + googleId + "/" + username));

        LocalDate lastUpdate = uNameRepository.findById(googleId).get().getLastUpdate().toLocalDate();
        String newUpdateDate = LocalDateTime.of(lastUpdate.plusMonths(1),
                LocalTime.of(0,0,0,0)).toString();

        mockMvc.perform(put("/api/v1/set_name/" + googleId + "/" + secondUsername))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string(newUpdateDate));

        String currentUsername = uNameRepository.findById(googleId).get().getName();

        assertThat(username).isEqualTo(currentUsername);
        assertThat(secondUsername).isNotEqualTo(currentUsername);
    }
    @Test
    void shouldReturnNotFoundWhenAnonimUserTriesToSetUsername() throws Exception {
        String username = "good_morning";

        String responseContent  = mockMvc.perform(get("/api/v1/anonim_user_id"))
                .andReturn().getResponse().getContentAsString();
        UserDTO userDTO = objectMapper.readValue(responseContent, UserDTO.class);
        User anonimUser = userRepository.findById(userDTO.getUserId()).get();

        String anonimUserId = anonimUser.getUserId();

        String anonimUsername = uNameRepository.findById(anonimUserId).get().getName();

        mockMvc.perform(put("/api/v1/set_name/" + anonimUserId + "/" + username))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Passed id belongs to guest."));

        String anonimUsernameAfterRequest = uNameRepository.findById(anonimUserId).get().getName();

        assertThat(anonimUsername).isEqualTo(anonimUsernameAfterRequest);
    }

    @Test
    void shouldReturnNotFoundWhenInvalidUserIdIsProvided() throws Exception {
        List<GameStatsDTO> gameStatsDTOList = createGameStatsDTOList();

        String googleId = "not_valid_id";

        mockMvc.perform(put("/api/v1/statsjson/" + googleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gameStatsDTOList)))
                .andExpect(status().isNotFound());
    }
    @Test
    void shouldUpdateStatisticsSuccessfullyWhenValidDataIsProvided() throws Exception {
        List<GameStatsDTO> gameStatsDTOList = createGameStatsDTOList();

        String googleId = createUser();

        mockMvc.perform(put("/api/v1/statsjson/" + googleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gameStatsDTOList)))
                .andExpect(status().isNoContent());
    }
    @Test
    void shouldUpdateStatisticsSuccessfullyForMultipleGameStats() throws Exception {
        List<GameStatsDTO> gameStatsDTOList = createGameStatsDTOList();

        String googleId = createUser();

        Stats statsOfGoogleUserBeforeUpdate = statsRepository.findAll().stream().filter(stats -> stats.getUser().getUserId().equals(googleId)).toList().getFirst();

        assertStatsBeforeUpdate(statsOfGoogleUserBeforeUpdate, googleId);

        Map<String, List<ModeStats>> modeStatsGroupedBeforeUpdate = modeStatsRepository.findAll().stream()
                .filter(stats -> stats.getUser().getUserId().equals(googleId))
                .filter(stats -> stats.getMode().equals("COUNTRIES_1900") || stats.getMode().equals("COUNTRIES_1939"))
                .collect(Collectors.groupingBy(ModeStats::getMode));

        assertModeStatsBeforeUpdate(modeStatsGroupedBeforeUpdate, googleId);

        Map<String, List<Score>> scoreGroupedBeforeUpdate = scoreRepository.findAll().stream().filter(score -> score.getUser().getUserId().equals(googleId))
                .filter(score -> score.getMode().equals("COUNTRIES_1900") || score.getMode().equals("COUNTRIES_1939"))
                .collect(Collectors.groupingBy(Score::getMode));

        assertScoreBeforeUpdate(scoreGroupedBeforeUpdate, googleId);

        mockMvc.perform(put("/api/v1/statsjson/" + googleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gameStatsDTOList)))
                .andExpect(status().isNoContent());

        Stats statsOfGoogleUserAfterUpdate = statsRepository.findAll().stream().filter(stats -> stats.getUser().getUserId().equals(googleId)).toList().getFirst();

        assertStatsAfterUpdate(statsOfGoogleUserAfterUpdate, googleId, gameStatsDTOList);

        Map<String, List<ModeStats>> modeStatsGroupedAfterUpdate = modeStatsRepository.findAll().stream()
                .filter(stats -> stats.getUser().getUserId().equals(googleId))
                .filter(stats -> stats.getMode().equals("COUNTRIES_1900") || stats.getMode().equals("COUNTRIES_1939"))
                .collect(Collectors.groupingBy(ModeStats::getMode));

        assertModeStatsAfterUpdate(modeStatsGroupedAfterUpdate, googleId);

        Map<String, List<Score>> scoreGroupedAfterUpdate = scoreRepository.findAll().stream().filter(score -> score.getUser().getUserId().equals(googleId))
                .filter(score -> score.getMode().equals("COUNTRIES_1900") || score.getMode().equals("COUNTRIES_1939"))
                .collect(Collectors.groupingBy(Score::getMode));

        assertScoreAfterUpdate(scoreGroupedAfterUpdate, googleId);

    }
    @Test
    void shouldReturnBestScoresWhenUserExists() throws Exception {
        String googleId = createUser();

        mockMvc.perform(get("/api/v1/bestscore/" + googleId))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(Mode.values().length)));

    }
    @Test
    void shouldReturnNotFoundWhenGettingBestScoresForNonexistentUser() throws Exception {
        String googleId = "google_user_id_that_doesnt_exist";

        mockMvc.perform(get("/api/v1/bestscore/" + googleId))
                .andExpect(status().isNotFound());
    }
    @Test
    void shouldReturnStatsWhenUserExists() throws Exception {
        String googleId = createUser();

        String responseContent = mockMvc.perform(get("/api/v1/stats/" + googleId))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        StatsDTO statsDTO = objectMapper.readValue(responseContent, StatsDTO.class);

        statsDTO.setUserId(googleId); //setting userId because StatsDTO have JsonIgnore so it is mapped to null by statsMapper
        assertStatsBeforeUpdate(statsMapper.statsDTOToStats(statsDTO), googleId);

    }
    @Test
    void shouldReturnNotFoundWhenGettingStatsForNonexistentUser() throws Exception {
        String googleId = "google_user_id_that_doesnt_exist";

        mockMvc.perform(get("/api/v1/bestscore/" + googleId))
                .andExpect(status().isNotFound());
    }
    @Test
    void shouldReturnAllStatsWhenUserExists() throws Exception {
        String googleId = createUser();
        User user = userRepository.findById(googleId).get();

        String responseContent = mockMvc.perform(get("/api/v1/all_stats/" + googleId))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<ModeStatsDTO> modeStatsDTOS = objectMapper.readValue(
                responseContent,
                new TypeReference<List<ModeStatsDTO>>() {}
        );


        List<ModeStats> modeStatsList = modeStatsDTOS.stream().map(modeStatsDTO -> {
            ModeStats modeStats = modeStatsMapper.modeStatsDTOtoModeStats(modeStatsDTO);
            modeStats.setUser(user);
            return modeStats;
        }).toList();

        assertAllModeStatsBeforeUpdate(modeStatsList, user);
    }
    @Test
    void shouldReturnNotFoundWhenGettingAllStatsForNonexistentUser() throws Exception {
        String googleId = "google_user_id_that_doesnt_exist";

        mockMvc.perform(get("/api/v1/all_stats/" + googleId))
                .andExpect(status().isNotFound());

    }
    @Test
    void shouldReturnWonGamesWhenUserExists() throws Exception {
        String googleId = createUser();

        String responseContent = mockMvc.perform(get("/api/v1/won_games/" + googleId))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<WonGameDTO> wonGameDTOS = objectMapper.readValue(
                responseContent,
                new TypeReference<List<WonGameDTO>>() {}
        );


        Set<Mode> modeSet = new HashSet<>(Arrays.asList(Mode.values()));

        assertThat(wonGameDTOS.size()).isEqualTo(modeSet.size());

        for(WonGameDTO wonGameDTO : wonGameDTOS) {
            assertThat(wonGameDTO.isWon()).isFalse();
            modeSet.remove(Mode.valueOf(wonGameDTO.getMode()));
        }
        assertThat(modeSet).isEmpty();
    }
    @Test
    void shouldReturnNotFoundWhenGettingWonGamesForNonexistentUser() throws Exception {
        String googleId = "google_user_id_that_doesnt_exist";

        mockMvc.perform(get("/api/v1/won_games/" + googleId))
                .andExpect(status().isNotFound());
    }
    private String createUser() throws Exception {
        String googleId = "TESTING";

        mockMvc.perform(post("/api/v1/google/" + googleId));
        return googleId;
    }
    private static List<GameStatsDTO> createGameStatsDTOList() {
        List<GameStatsDTO> gameStatsDTOList = new ArrayList<>();
        gameStatsDTOList.add(GameStatsDTO.builder()
                .gameMode("COUNTRIES_1900")
                .wonGame(false)
                .scoredPoints(18)
                .timePlayedSeconds(29)
                .build());
        gameStatsDTOList.add(GameStatsDTO.builder()
                .gameMode("COUNTRIES_1939")
                .wonGame(false)
                .scoredPoints(10)
                .timePlayedSeconds(18)
                .build());
        return gameStatsDTOList;
    }
    private static void assertScoreBeforeUpdate(Map<String, List<Score>> statsGroupedBeforeUpdate, String googleId) {
        Score scoreOfCountries1900BeforeUpdate = statsGroupedBeforeUpdate.get("COUNTRIES_1900").getFirst();

        assertThat(scoreOfCountries1900BeforeUpdate.getUser().getUserId()).isEqualTo(googleId);
        assertThat(scoreOfCountries1900BeforeUpdate.getMode()).isEqualTo("COUNTRIES_1900");
        assertThat(scoreOfCountries1900BeforeUpdate.getBestScore()).isEqualTo(0);

        Score scoreOfCountries1939BeforeUpdate = statsGroupedBeforeUpdate.get("COUNTRIES_1939").getFirst();

        assertThat(scoreOfCountries1939BeforeUpdate.getUser().getUserId()).isEqualTo(googleId);
        assertThat(scoreOfCountries1939BeforeUpdate.getMode()).isEqualTo("COUNTRIES_1939");
        assertThat(scoreOfCountries1939BeforeUpdate.getBestScore()).isEqualTo(0);
    }
    private static void assertScoreAfterUpdate(Map<String, List<Score>> scoreGroupedAfterUpdate, String googleId) {
        Score scoreOfCountries1900AfterUpdate = scoreGroupedAfterUpdate.get("COUNTRIES_1900").getFirst();

        assertThat(scoreOfCountries1900AfterUpdate.getUser().getUserId()).isEqualTo(googleId);
        assertThat(scoreOfCountries1900AfterUpdate.getMode()).isEqualTo("COUNTRIES_1900");
        assertThat(scoreOfCountries1900AfterUpdate.getBestScore()).isEqualTo(18);

        Score scoreOfCountries1939AfterUpdate = scoreGroupedAfterUpdate.get("COUNTRIES_1939").getFirst();

        assertThat(scoreOfCountries1939AfterUpdate.getUser().getUserId()).isEqualTo(googleId);
        assertThat(scoreOfCountries1939AfterUpdate.getMode()).isEqualTo("COUNTRIES_1939");
        assertThat(scoreOfCountries1939AfterUpdate.getBestScore()).isEqualTo(10);
    }
    private static void assertModeStatsBeforeUpdate(Map<String, List<ModeStats>> modeStatsGroupedBeforeUpdate, String googleId) {
        ModeStats modeStatsOfCountries1900BeforeUpdate = modeStatsGroupedBeforeUpdate.get("COUNTRIES_1900").getFirst();

        assertThat(modeStatsOfCountries1900BeforeUpdate.getUser().getUserId()).isEqualTo(googleId);
        assertThat(modeStatsOfCountries1900BeforeUpdate.getTotalGamePlayed()).isEqualTo(0);
        assertThat(modeStatsOfCountries1900BeforeUpdate.getTimePlayed()).isEqualTo(0);
        assertThat(modeStatsOfCountries1900BeforeUpdate.getTotalScoredPoints()).isEqualTo(0);
        assertThat(modeStatsOfCountries1900BeforeUpdate.getNumberOfWonGames()).isEqualTo(0);
        assertThat(modeStatsOfCountries1900BeforeUpdate.getAvgScore()).isEqualTo(new BigDecimal("0.00"));

        ModeStats modeStatsOfCountries1939BeforeUpdate = modeStatsGroupedBeforeUpdate.get("COUNTRIES_1939").getFirst();

        assertThat(modeStatsOfCountries1939BeforeUpdate.getUser().getUserId()).isEqualTo(googleId);
        assertThat(modeStatsOfCountries1939BeforeUpdate.getTotalGamePlayed()).isEqualTo(0);
        assertThat(modeStatsOfCountries1939BeforeUpdate.getTimePlayed()).isEqualTo(0);
        assertThat(modeStatsOfCountries1939BeforeUpdate.getTotalScoredPoints()).isEqualTo(0);
        assertThat(modeStatsOfCountries1939BeforeUpdate.getNumberOfWonGames()).isEqualTo(0);
        assertThat(modeStatsOfCountries1939BeforeUpdate.getAvgScore()).isEqualTo(new BigDecimal("0.00"));
    }
    private static void assertModeStatsAfterUpdate(Map<String, List<ModeStats>> modeStatsGroupedAfterUpdate, String googleId) {
        ModeStats modeStatsOfCountries1900AfterUpdate = modeStatsGroupedAfterUpdate.get("COUNTRIES_1900").getFirst();

        assertThat(modeStatsOfCountries1900AfterUpdate.getUser().getUserId()).isEqualTo(googleId);
        assertThat(modeStatsOfCountries1900AfterUpdate.getTotalGamePlayed()).isEqualTo(1);
        assertThat(modeStatsOfCountries1900AfterUpdate.getTimePlayed()).isEqualTo(29);
        assertThat(modeStatsOfCountries1900AfterUpdate.getTotalScoredPoints()).isEqualTo(18);
        assertThat(modeStatsOfCountries1900AfterUpdate.getNumberOfWonGames()).isEqualTo(0);
        assertThat(modeStatsOfCountries1900AfterUpdate.getAvgScore()).isEqualTo(new BigDecimal("18.00"));

        ModeStats modeStatsOfCountries1939AfterUpdate = modeStatsGroupedAfterUpdate.get("COUNTRIES_1939").getFirst();

        assertThat(modeStatsOfCountries1939AfterUpdate.getUser().getUserId()).isEqualTo(googleId);
        assertThat(modeStatsOfCountries1939AfterUpdate.getTotalGamePlayed()).isEqualTo(1);
        assertThat(modeStatsOfCountries1939AfterUpdate.getTimePlayed()).isEqualTo(18);
        assertThat(modeStatsOfCountries1939AfterUpdate.getTotalScoredPoints()).isEqualTo(10);
        assertThat(modeStatsOfCountries1939AfterUpdate.getNumberOfWonGames()).isEqualTo(0);
        assertThat(modeStatsOfCountries1939AfterUpdate.getAvgScore()).isEqualTo(new BigDecimal("10.00"));
    }
    private static void assertStatsBeforeUpdate(Stats statsOfGoogleUserBeforeUpdate, String googleId) {
        assertThat(statsOfGoogleUserBeforeUpdate.getUserId()).isEqualTo(googleId);
        assertThat(statsOfGoogleUserBeforeUpdate.getTotalGamePlayed()).isEqualTo(0);
        assertThat(statsOfGoogleUserBeforeUpdate.getTimePlayed()).isEqualTo(0);
        assertThat(statsOfGoogleUserBeforeUpdate.getTotalScoredPoints()).isEqualTo(0);
        assertThat(statsOfGoogleUserBeforeUpdate.getNumberOfWonGames()).isEqualTo(0);
        assertThat(statsOfGoogleUserBeforeUpdate.getAvgScore()).isEqualTo(new BigDecimal("0.00"));
    }
    private static void assertStatsAfterUpdate(Stats statsOfGoogleUserAfterUpdate, String googleId, List<GameStatsDTO> gameStatsDTOList) {
        assertThat(statsOfGoogleUserAfterUpdate.getUserId()).isEqualTo(googleId);
        assertThat(statsOfGoogleUserAfterUpdate.getTotalGamePlayed()).isEqualTo(gameStatsDTOList.size());
        assertThat(statsOfGoogleUserAfterUpdate.getTimePlayed()).isEqualTo(29 + 18);
        assertThat(statsOfGoogleUserAfterUpdate.getTotalScoredPoints()).isEqualTo(18 + 10);
        assertThat(statsOfGoogleUserAfterUpdate.getNumberOfWonGames()).isEqualTo(0);
        assertThat(statsOfGoogleUserAfterUpdate.getAvgScore()).isEqualTo((new BigDecimal(18).add(new BigDecimal(10))
                .divide(new BigDecimal(statsOfGoogleUserAfterUpdate.getTotalGamePlayed()), 2, RoundingMode.HALF_UP)));
    }
    private static void assertAllModeStatsBeforeUpdate(List<ModeStats> modeStatsList, User user) {
        for(ModeStats modeStats : modeStatsList) {
            assertThat(modeStats.getUser()).isEqualTo(user);
            assertThat(modeStats.getTotalGamePlayed()).isEqualTo(0);
            assertThat(modeStats.getTimePlayed()).isEqualTo(0);
            assertThat(modeStats.getTotalScoredPoints()).isEqualTo(0);
            assertThat(modeStats.getNumberOfWonGames()).isEqualTo(0);
            assertThat(modeStats.getAvgScore()).isEqualTo(new BigDecimal("0.00"));
        }
    }
}
