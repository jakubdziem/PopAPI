package com.dziem.popapi.mapper;

import com.dziem.popapi.model.Stats;
import com.dziem.popapi.model.StatsDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StatsMapper {
    StatsDTO statsToStatsDTO(Stats stats);
}
