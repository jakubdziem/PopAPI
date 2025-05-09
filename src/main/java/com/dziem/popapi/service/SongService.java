package com.dziem.popapi.service;

import com.dziem.popapi.dto.SongDTO;

import java.util.List;

public interface SongService {
    List<SongDTO> getTop200Songs();

    List<SongDTO> getTop200SongsGenre(String genre);
}
