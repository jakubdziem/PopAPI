package com.dziem.popapi.mapper;

import com.dziem.popapi.model.webpage.ActiveUsersStats;
import com.dziem.popapi.model.webpage.WeeklyActiveUsersStats;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;

@Mapper(componentModel = "spring", imports = { LocalDate.class })
public interface ActiveUserStatsMapper {
    @Mapping(expression = "java(activeUsersStats.isNewUser())", target = "newUser")
    @Mapping(expression = "java(LocalDate.now())", target = "weekStartDate")
    WeeklyActiveUsersStats activeUsersStatsToWeeklyActiveUsersStats(ActiveUsersStats activeUsersStats);
    @Mapping(expression = "java(weeklyActiveUsersStats.isNewUser())", target = "newUser")
    ActiveUsersStats weeklyActiveUsersStatsToActiveUsersStats(WeeklyActiveUsersStats weeklyActiveUsersStats);
}
