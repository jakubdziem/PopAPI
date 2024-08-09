package com.dziem.popapi.service;

import com.dziem.popapi.mapper.ArtistMapper;
import com.dziem.popapi.model.Artist;
import com.dziem.popapi.model.ArtistDTO;
import com.dziem.popapi.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {
    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;
    @Override
    public List<ArtistDTO> getTop200Artists() {
        List<Artist> artists = artistRepository.findAll();
        List<ArtistDTO> artistDTOS = new ArrayList<>();
        for(Artist artist : artists) {
            artistDTOS.add(artistMapper.artistToArtistDTO(artist));
        }
        return artistDTOS;
    }
}
