package com.dziem.popapi.service;

import com.dziem.popapi.mapper.ArtistMapper;
import com.dziem.popapi.model.Artist;
import com.dziem.popapi.dto.ArtistDTO;
import com.dziem.popapi.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
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

    @Override
    public List<ArtistDTO> getTop200LatestArtists() {
        List<ArtistDTO> artistDTOS = new ArrayList<>();
        List<Date> allDates = artistRepository.findAllDates();
        List<LocalDate> lastUpdates = new ArrayList<>();
        for(Date date : allDates) {
            lastUpdates.add(date.toLocalDate());
        }
        LocalDate max = LocalDate.MIN;
        for(LocalDate update : lastUpdates) {
            if(update.isAfter(max)) {
                max = update;
            }
        }
        List<Artist> artists = artistRepository.findAllArtistsFromCertainUpdate(max);
        for(Artist artist : artists) {
            artistDTOS.add(artistMapper.artistToArtistDTO(artist));
        }
        return artistDTOS;
    }
}
