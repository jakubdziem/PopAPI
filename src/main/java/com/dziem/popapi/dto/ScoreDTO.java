package com.dziem.popapi.dto;

import com.dziem.popapi.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;


@Data
public class ScoreDTO {
    @JsonIgnore
    private Long id;
    private Integer bestScore;
    private String mode;
    @JsonIgnore
    private User user;
}
