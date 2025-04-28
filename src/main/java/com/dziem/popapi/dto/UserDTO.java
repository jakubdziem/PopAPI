package com.dziem.popapi.dto;

import com.dziem.popapi.model.ModeStats;
import com.dziem.popapi.model.Score;
import com.dziem.popapi.model.Stats;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private String userId;
    @JsonIgnore
    private Stats statistics;
    @JsonIgnore
    private List<Score> bestScores;
    private String name;
    @JsonIgnore
    boolean guest;
    @JsonIgnore
    private List<ModeStats> modeStats;
}
