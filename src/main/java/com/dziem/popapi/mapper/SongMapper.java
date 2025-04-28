package com.dziem.popapi.mapper;

import com.dziem.popapi.model.Song;
import com.dziem.popapi.dto.SongDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface SongMapper {
    SongDTO songToSongDTO(Song song);
}
