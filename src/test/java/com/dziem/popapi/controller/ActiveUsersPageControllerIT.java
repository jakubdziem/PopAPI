package com.dziem.popapi.controller;

import com.dziem.popapi.dto.webpage.ActiveUsersStatsDTO;
import com.dziem.popapi.mapper.ActiveUserStatsMapper;
import com.dziem.popapi.repository.WeeklyActiveUsersStatsRepository;
import com.dziem.popapi.service.ActiveUsersPageService;
import com.dziem.popapi.service.StatsPageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.dziem.popapi.controller.StatsPageController.ALL_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@ActiveProfiles("test")
public class ActiveUsersPageControllerIT {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ActiveUsersPageService activeUsersPageService;
    @Autowired
    private WeeklyActiveUsersStatsRepository weeklyActiveUsersStatsRepository;
    @Autowired
    private ActiveUserStatsMapper activeUserStatsMapper;
    @Autowired
    private StatsPageService statsPageService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    @Test
    void shouldReturnAllTimeActiveUsersAndStats_whenSelectedWeekIsAllTime() throws Exception {
        MvcResult result = mockMvc.perform(get("/active?selectedWeekActive=ALL_TIME"))
                .andExpect(status().isOk())
                .andReturn();
        Map<String, Object> model = result.getModelAndView().getModel();
        List<LocalDate> weeks = activeUsersPageService.getWeeks();
        List<ActiveUsersStatsDTO> activeUsersStatsThisWeek = activeUsersPageService.getActiveUsersStatsThisWeek();
        assertActiveUsersStatsForWeekAreCorrect(activeUsersStatsThisWeek, model, ALL_TIME, weeks);
    }
    @Test
    void shouldReturnWeeklyActiveUsersAndStats_whenSelectedWeekIsProvided() throws Exception {
        List<LocalDate> weeks = activeUsersPageService.getWeeks();
        String week = weeks.getFirst().toString();
        MvcResult result = mockMvc.perform(get("/active?selectedWeekActive=" + week))
                .andExpect(status().isOk())
                .andReturn();
        Map<String, Object> model = result.getModelAndView().getModel();
        List<ActiveUsersStatsDTO> activeUsersStatsThisWeek = weeklyActiveUsersStatsRepository.getAllActiveUsersStatsFromWeek(LocalDate.parse(week))
                .stream().map(activeUserStatsMapper::weeklyActiveUsersStatsToActiveUsersStats).toList();
        assertActiveUsersStatsForWeekAreCorrect(activeUsersStatsThisWeek, model, week, weeks);
    }
    @Test
    void shouldHandleNullSelectedWeekGracefully() throws Exception {
        MvcResult result = mockMvc.perform(get("/active"))
                .andExpect(status().isOk())
                .andReturn();
        Map<String, Object> model = result.getModelAndView().getModel();
        List<LocalDate> weeks = activeUsersPageService.getWeeks();
        List<ActiveUsersStatsDTO> activeUsersStatsThisWeek = activeUsersPageService.getActiveUsersStatsThisWeek();
        assertActiveUsersStatsForWeekAreCorrect(activeUsersStatsThisWeek, model, ALL_TIME, weeks);
    }
    private void assertActiveUsersStatsForWeekAreCorrect(
            List<ActiveUsersStatsDTO> activeUsersStatsThisWeek, Map<String, Object> model,
            String week, List<LocalDate> weeks) {
        int activeUsersCount = activeUsersStatsThisWeek.size();
        int activeOldUsersCount = activeUsersStatsThisWeek.stream().filter(a -> !a.isNewUser()).toList().size();
        assertThat(model.get("selectedWeekActive").toString()).isEqualTo(week);
        assertThat(model.get("weeks")).isEqualTo(weeks);
        assertThat(model.get("activeUsers")).isEqualTo(activeUsersStatsThisWeek);
        assertThat(model.get("activeOldUsersCount")).isEqualTo(activeOldUsersCount);
        assertThat(model.get("activeUsersCount")).isEqualTo(activeUsersCount);
        assertThat(model.get("thisWeekUsers")).isEqualTo(activeUsersCount - activeOldUsersCount);
        assertThat(model.get("activeUsersStatsCombined")).isEqualTo(statsPageService.getDifferenceStatsOfAllUsersCombined(week));
    }
    @Test
    @Transactional
    @Rollback
    void shouldReturnEmptyWeekList_whenNoWeeksAvailable() throws Exception {
        weeklyActiveUsersStatsRepository.deleteAll();
        String week = "1000-01-01";
        MvcResult result = mockMvc.perform(get("/active?selectedWeekActive=" + week))
                .andExpect(status().isOk())
                .andReturn();
        Map<String, Object> model = result.getModelAndView().getModel();
        List<LocalDate> weeks = activeUsersPageService.getWeeks();
        assertThat(model.get("weeks")).isEqualTo(weeks);
        assertThat(weeks).isEmpty();
        assertThat(model.get("selectedWeekActive").toString()).isEqualTo(week);

    }
    @Test
    void shouldUseEmptyWeeklyActiveUsers_whenNoMatchingWeekFound() throws Exception {
        List<LocalDate> weeks = activeUsersPageService.getWeeks();
        LocalDate week = LocalDate.of(1000, 1, 1);
        assertThat(weeks.contains(week)).isFalse();
        MvcResult result = mockMvc.perform(get("/active?selectedWeekActive=" + week))
                .andExpect(status().isOk())
                .andReturn();
        Map<String, Object> model = result.getModelAndView().getModel();
        List<ActiveUsersStatsDTO> activeUsersStatsThisWeek = activeUsersPageService.getActiveUsersStatsFromWeek(week.toString());
        assertThat(activeUsersStatsThisWeek).isEmpty();
        assertThat(model.get("activeUsers")).isEqualTo(activeUsersStatsThisWeek);
    }
}
