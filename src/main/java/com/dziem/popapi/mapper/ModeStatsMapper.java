package com.dziem.popapi.mapper;

import com.dziem.popapi.model.ModeStats;
import com.dziem.popapi.model.ModeStatsDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface ModeStatsMapper {
    ModeStatsDTO modeStatsToModeStatsDTO(ModeStats modeStats);
}
