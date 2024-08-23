package com.dziem.popapi.model;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private String userId;
    private Stats statistics;
    private List<Score> bestScores;
    private UName uName;
    boolean guest;
}
