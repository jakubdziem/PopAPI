package com.dziem.popapi.service;

import com.dziem.popapi.model.ArtistDTO;

import java.util.List;

public interface ArtistService {
    List<ArtistDTO> getTop200Artists();
    List<ArtistDTO> getTop200LatestArtists();
}
