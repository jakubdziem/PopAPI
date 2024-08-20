package com.dziem.popapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;


@Data
public class ScoreDTO {
    @JsonIgnore
    private Long id;
    private Integer bestScore;
    private String mode;
    private User user;
}
