package com.dziem.popapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class LeaderboardDTO implements Comparable<LeaderboardDTO> {
    @JsonIgnore
    private Long id;
    private String userId;
    private String name;
    private String mode;
    private Integer score;

    @Override
    public int compareTo(LeaderboardDTO leaderboardDTO) {
        return -score.compareTo(leaderboardDTO.score);
    }
    //it's reversed, because I couldn't find using reversed() in lambda
}
