package com.dziem.popapi.controller;

import com.dziem.popapi.model.Mode;
import com.dziem.popapi.service.StatsPageChartService;
import com.dziem.popapi.service.StatsPageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.http.MediaType;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.dziem.popapi.controller.StatsPageController.ALL_TIME;
import static com.dziem.popapi.controller.StatsPageController.DEFAULT_ATTRIBUTE;
import static com.dziem.popapi.service.StatsPageService.COMBINED_STATS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class StatsPageControllerIT {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private StatsPageService statsPageService;
    @Autowired
    private StatsPageChartService statsPageChartService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void shouldInitializeSessionAttributesWithDefaultValues_whenNoRequestParametersProvided() throws Exception {
        MvcResult result = mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();
        Map<String, Object> model = result.getModelAndView().getModel();
        assertThat(model.get("selectedWeek")).isEqualTo(ALL_TIME);
        assertThat(model.get("selectedMode")).isEqualTo(COMBINED_STATS);
        assertThat(model.get("attributeSelect")).isEqualTo(DEFAULT_ATTRIBUTE);
    }
    @Test
    void shouldUpdateSessionAttributes_whenAllRequestParametersProvided() throws Exception {
        List<LocalDate> weeks = statsPageService.getWeeks();
        String week = weeks.getFirst().toString();
        String mode = Mode.FORMULA_TOP_TEAMS_GP.toString();
        String attribute = "avgScore";
        MvcResult result = mockMvc.perform(get("/stats?selectedWeek=" + week + "&selectedMode=" + mode + "&attributeSelect=" + attribute))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();

        Map<String, Object> model = result.getModelAndView().getModel();
        assertThat(model.get("selectedWeek")).isEqualTo(week);
        assertThat(model.get("selectedMode")).isEqualTo(mode);
        assertThat(model.get("attributeSelect")).isEqualTo(attribute);
    }
    @Test
    void shouldUpdateSessionAttributesAndInitializeOtherToDefault_whenModeAndWeekRequestParametersProvided() throws Exception {
        List<LocalDate> weeks = statsPageService.getWeeks();
        String week = weeks.getFirst().toString();
        String mode = Mode.FORMULA_TOP_TEAMS_GP.toString();
        MvcResult result = mockMvc.perform(get("/stats?selectedWeek=" + week + "&selectedMode=" + mode))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();

        Map<String, Object> model = result.getModelAndView().getModel();
        assertThat(model.get("selectedWeek")).isEqualTo(week);
        assertThat(model.get("selectedMode")).isEqualTo(mode);
        assertThat(model.get("attributeSelect")).isEqualTo(DEFAULT_ATTRIBUTE);
    }
    @Test
    void shouldUpdateSessionAttributesAndInitializeOtherToDefault_whenModeAndAttributeRequestParametersProvided() throws Exception {
        String mode = Mode.FORMULA_TOP_TEAMS_GP.toString();
        String attribute = "avgScore";
        MvcResult result = mockMvc.perform(get("/stats?selectedMode=" + mode + "&attributeSelect=" + attribute))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();

        Map<String, Object> model = result.getModelAndView().getModel();
        assertThat(model.get("selectedWeek")).isEqualTo(ALL_TIME);
        assertThat(model.get("selectedMode")).isEqualTo(mode);
        assertThat(model.get("attributeSelect")).isEqualTo(attribute);
    }
    @Test
    void shouldUpdateSessionAttributesAndInitializeOtherToDefault_whenWeekAndAttributeRequestParametersProvided() throws Exception {
        List<LocalDate> weeks = statsPageService.getWeeks();
        String week = weeks.getFirst().toString();
        String attribute = "avgScore";
        MvcResult result = mockMvc.perform(get("/stats?selectedWeek=" + week + "&attributeSelect=" + attribute))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();

        Map<String, Object> model = result.getModelAndView().getModel();
        assertThat(model.get("selectedWeek")).isEqualTo(week);
        assertThat(model.get("selectedMode")).isEqualTo(COMBINED_STATS);
        assertThat(model.get("attributeSelect")).isEqualTo(attribute);
    }
    @Test
    void shouldUpdateSessionAttributeAndInitializeOtherToDefault_whenModeRequestParameterProvided() throws Exception {
        String mode = Mode.FORMULA_TOP_TEAMS_GP.toString();
        MvcResult result = mockMvc.perform(get("/stats?selectedMode=" + mode))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();

        Map<String, Object> model = result.getModelAndView().getModel();
        assertThat(model.get("selectedWeek")).isEqualTo(ALL_TIME);
        assertThat(model.get("selectedMode")).isEqualTo(mode);
        assertThat(model.get("attributeSelect")).isEqualTo(DEFAULT_ATTRIBUTE);
    }
    @Test
    void shouldUpdateSessionAttributeAndInitializeOtherToDefault_whenWeekRequestParameterProvided() throws Exception {
        List<LocalDate> weeks = statsPageService.getWeeks();
        String week = weeks.getFirst().toString();
        MvcResult result = mockMvc.perform(get("/stats?selectedWeek=" + week))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();

        Map<String, Object> model = result.getModelAndView().getModel();
        assertThat(model.get("selectedWeek")).isEqualTo(week);
        assertThat(model.get("selectedMode")).isEqualTo(COMBINED_STATS);
        assertThat(model.get("attributeSelect")).isEqualTo(DEFAULT_ATTRIBUTE);
    }
    @Test
    void shouldUpdateSessionAttributeAndInitializeOtherToDefault_whenAttributeRequestParameterProvided() throws Exception {
        String attribute = "avgScore";
        MvcResult result = mockMvc.perform(get("/stats?attributeSelect=" + attribute))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();

        Map<String, Object> model = result.getModelAndView().getModel();
        assertThat(model.get("selectedWeek")).isEqualTo(ALL_TIME);
        assertThat(model.get("selectedMode")).isEqualTo(COMBINED_STATS);
        assertThat(model.get("attributeSelect")).isEqualTo(attribute);
    }
    @Test
    void shouldShowStatsPageWithDefaultSessionAttributes_whenNoRequestParametersProvided() throws Exception {
        MvcResult result = mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();

        Map<String, Object> model = result.getModelAndView().getModel();
        List<String> modes = Arrays.stream(Mode.values()).map(Enum::toString).toList();
        List<LocalDate> weeks = statsPageService.getWeeks();
        assertStatsPageModelAttributes(model, ALL_TIME, COMBINED_STATS, DEFAULT_ATTRIBUTE, modes, weeks);
    }

    private void assertStatsPageModelAttributes(Map<String, Object> model, String week, String mode, String selectedAttribute, List<String> modes, List<LocalDate> weeks) {
        assertThat(model.get("selectedWeek")).isEqualTo(week);
        assertThat(model.get("selectedMode")).isEqualTo(mode);
        assertThat(model.get("attributeSelect")).isEqualTo(selectedAttribute);
        assertThat(model.get("modes")).isEqualTo(modes);
        assertThat(model.get("weeks")).isEqualTo(weeks);
        assertThat(model.get("differenceUsersSummed")).isEqualTo(statsPageService.getDifferenceUsersSummed(week));
        assertThat(model.get("modesWithPositiveDifference")).isEqualTo(statsPageService.getModesWithPositiveDifference(week, modes));
        if (week.equals(ALL_TIME)) {
            assertThat(model.get("dailyUsers")).isEqualTo(statsPageChartService.getTodayUserSummedDifference());
            assertThat(model.get("users"))
                    .usingRecursiveComparison()
                    .isEqualTo(statsPageService.getUsersSummedCurrent());
            if (mode.equals(COMBINED_STATS)) {
                assertThat(model.get("overallStats"))
                        .usingRecursiveComparison()
                        .isEqualTo(statsPageService.getStatsWithUNameOfAllUsersCurrent());
                assertThat(model.get("overallStatsOfUsersCombined"))
                        .usingRecursiveComparison()
                        .isEqualTo(statsPageService.getStatsOfAllUsersCombinedCurrent());
                assertThat(model.get("differenceOverallStatsOfUsersCombined"))
                        .isEqualTo(statsPageService.getDifferenceStatsOfAllUsersCombined(week));
            } else {
                assertThat(model.get("overallStats"))
                        .usingRecursiveComparison()
                        .isEqualTo(statsPageService.getAllGameStatsWithUNameOfAllUsersCurrent());
                assertThat(model.get("overallStatsOfUsersCombined"))
                        .usingRecursiveComparison()
                        .isEqualTo(statsPageService.getGameStatsOffAllUsersCombinedCurrent());
                assertThat(model.get("differenceOverallStatsOfUsersCombined"))
                        .isEqualTo(statsPageService.getDifferenceGameStatsOffAllUsersCombined(week));
            }
        }
    }

    @Test
    void shouldShowStatsPageWithProvidedSessionAttributes_whenRequestParametersProvided() throws Exception {
        List<LocalDate> weeks = statsPageService.getWeeks();
        List<String> modes = Arrays.stream(Mode.values()).map(Enum::toString).toList();
        String week = weeks.getFirst().toString();
        String mode = Mode.FORMULA_TOP_TEAMS_GP.toString();
        String attribute = "avgScore";
        MvcResult result = mockMvc.perform(get("/stats?selectedWeek=" + week + "&selectedMode=" + mode + "&attributeSelect=" + attribute))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();

        Map<String, Object> model = result.getModelAndView().getModel();
        assertStatsPageModelAttributes(model, week, mode, attribute, modes, weeks);
    }

}
