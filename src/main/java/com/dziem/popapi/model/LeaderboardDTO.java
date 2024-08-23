package com.dziem.popapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class LeaderboardDTO {
    @JsonIgnore
    private Long id;
    private String userId;
    private String name;
    private String mode;
    private Integer score;
}
