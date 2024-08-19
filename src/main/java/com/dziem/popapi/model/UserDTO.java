package com.dziem.popapi.model;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UserDTO {
    private UUID userId;
    private Stats statistics;
    private List<Score> bestScores;
}
