package com.dziem.popapi.mapper;

import com.dziem.popapi.model.ModeStats;
import com.dziem.popapi.model.ModeStatsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface ModeStatsMapper {
    @Mapping(expression = "java(modeStats.getUser().getBestScores().stream().filter(score -> score.getMode().equals(modeStats.getMode())).toList().get(0).getBestScore())", target = "bestScore")
    ModeStatsDTO modeStatsToModeStatsDTO(ModeStats modeStats);

}
