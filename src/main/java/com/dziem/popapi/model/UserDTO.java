package com.dziem.popapi.model;

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
}
