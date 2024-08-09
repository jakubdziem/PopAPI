package com.dziem.popapi.mapper;

import com.dziem.popapi.model.YearAndPopulation;
import com.dziem.popapi.model.YearAndPopulationDTO;
import org.mapstruct.Mapper;


@Mapper
public interface YearAndPopulationMapper {
    YearAndPopulation yearAndPopulationDtoToYearAndPopulation(YearAndPopulationDTO dto);
    YearAndPopulationDTO yearAndPopulationToYearAndPopulationDto(YearAndPopulation yearAndPopulation);
}
