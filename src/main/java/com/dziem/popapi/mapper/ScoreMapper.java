package com.dziem.popapi.mapper;

import com.dziem.popapi.model.Score;
import com.dziem.popapi.model.ScoreDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScoreMapper {
    ScoreDTO scoreToScoreDTO(Score score);
    Score scoreDtoToScore(ScoreDTO scoreDTO);
}
