package com.dziem.popapi.dto;

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
    String imageSource;
    String imageSourceShort;
}
