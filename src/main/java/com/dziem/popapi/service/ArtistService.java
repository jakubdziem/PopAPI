package com.dziem.popapi.service;

import com.dziem.popapi.dto.ArtistDTO;

import java.util.List;

public interface ArtistService {
    List<ArtistDTO> getTop200Artists();
    List<ArtistDTO> getTop200LatestArtists();
}
