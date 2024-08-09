package com.dziem.popapi.mapper;

import com.dziem.popapi.model.Artist;
import com.dziem.popapi.model.ArtistDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArtistMapper {
    Artist artistDtoToArtist(ArtistDTO dto);
    ArtistDTO artistToArtistDTO(Artist artist);
}
