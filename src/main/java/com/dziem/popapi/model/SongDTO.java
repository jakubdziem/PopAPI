package com.dziem.popapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SongDTO {
    @JsonIgnore
    Long id;
    String songName;
    String artistName;
    String totalStreams;
    String imageUrl;
    @JsonIgnore
    String genre;
    @JsonIgnore
    LocalDate lastUpdate;
    String imageSource;
    String imageSourceShort;
}
