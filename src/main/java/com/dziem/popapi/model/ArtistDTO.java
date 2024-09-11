package com.dziem.popapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ArtistDTO {
    @JsonIgnore
    Long id;
    String artistName;
    String leadStreams;
    String imageUrl;
    @JsonIgnore
    LocalDate lastUpdate;
    @JsonIgnore
    private Integer tier;
}
