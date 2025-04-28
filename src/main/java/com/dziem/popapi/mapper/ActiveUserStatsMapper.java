package com.dziem.popapi.mapper;

import com.dziem.popapi.dto.webpage.ActiveUsersStatsDTO;
import com.dziem.popapi.model.webpage.WeeklyActiveUsersStats;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;

@Mapper(componentModel = "spring", imports = { LocalDate.class })
public interface ActiveUserStatsMapper {
    @Mapping(expression = "java(activeUsersStatsDTO.isNewUser())", target = "newUser")
    @Mapping(expression = "java(LocalDate.now())", target = "weekStartDate")
    WeeklyActiveUsersStats activeUsersStatsDTOToWeeklyActiveUsersStats(ActiveUsersStatsDTO activeUsersStatsDTO);
    @Mapping(expression = "java(weeklyActiveUsersStats.isNewUser())", target = "newUser")
    ActiveUsersStatsDTO weeklyActiveUsersStatsToActiveUsersStats(WeeklyActiveUsersStats weeklyActiveUsersStats);
}
