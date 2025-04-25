package com.dziem.popapi.mapper;

import com.dziem.popapi.model.Artist;
import com.dziem.popapi.dto.ArtistDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArtistMapper {
    ArtistDTO artistToArtistDTO(Artist artist);
}
