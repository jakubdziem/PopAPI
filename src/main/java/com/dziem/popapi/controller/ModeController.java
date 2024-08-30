package com.dziem.popapi.controller;

import com.dziem.popapi.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@RequiredArgsConstructor
@RestController
public class ModeController {
    private final CountryController countryController;
    private final SpotifyController spotifyController;
    @GetMapping("/api/v1/mode/{gameMode}")
    public List<BaseGameModelDTO> getElementsByGameMode(@PathVariable Mode gameMode) {
        return switch(gameMode) {
            case Mode.COUNTRIES_1900 -> convertCountryDTOtoBaseGameModelDTO("1900", false);
            case Mode.COUNTRIES_1939 -> convertCountryDTOtoBaseGameModelDTO("1939", false);
            case Mode.COUNTRIES_1989 -> convertCountryDTOtoBaseGameModelDTO("1989", false);
            case Mode.COUNTRIES_NORMAL -> convertCountryDTOtoBaseGameModelDTO(String.valueOf(LocalDate.now().getYear()),false);
            case Mode.COUNTRIES_FUTURE -> convertCountryDTOtoBaseGameModelDTO(String.valueOf(LocalDate.now().getYear() + 100), false);
            case Mode.COUNTRIES_CHAOS -> convertCountryDTOtoBaseGameModelDTO("", true);
            case Mode.SPOTIFY_TOP_ARTISTS -> convertArtistDTOStoBaseGameModelDTO();
            case Mode.SPOTIFY_TOP_SONGS_GENERAL -> convertSongDTOStoBaseGameModelDTO("General");
            case Mode.SPOTIFY_TOP_SONGS_POP -> convertSongDTOStoBaseGameModelDTO("Pop");
            case Mode.SPOTIFY_TOP_SONGS_HIP_HOP -> convertSongDTOStoBaseGameModelDTO("HipHop");
            case Mode.SPOTIFY_TOP_SONGS_ROCK -> convertSongDTOStoBaseGameModelDTO("Rock");
        };
    }
    private List<BaseGameModelDTO> convertCountryDTOtoBaseGameModelDTO(String year, boolean chaos) {
        List<BaseGameModelDTO> baseGameModelDTOS = new ArrayList<>();
        List<CountryDTO> countryDTOS;
        if(chaos) {
            countryDTOS = countryController.getPopChaos(true);
        } else {
            countryDTOS = countryController.getPopInDistinctYear(year);
        }
        for(CountryDTO countryDTO : countryDTOS) {
            BaseGameModelDTO baseGameModelDTO = BaseGameModelDTO.builder()
                    .name(countryDTO.getCountryName())
                    .comparableValue(countryDTO.getYearAndPopulations().getFirst().getPopulation())
                    .comparableValueLabel("population")
                    .imageUrl(countryDTO.getFlagUrl())
                    .build();
            baseGameModelDTOS.add(baseGameModelDTO);
        }
        return baseGameModelDTOS;
    }
    private List<BaseGameModelDTO> convertArtistDTOStoBaseGameModelDTO() {
        List<BaseGameModelDTO> baseGameModelDTOS = new ArrayList<>();
        List<ArtistDTO> artistDTOS = spotifyController.getTop200Artists();
        for(ArtistDTO artistDTO : artistDTOS) {
            BaseGameModelDTO baseGameModelDTO = BaseGameModelDTO.builder()
                    .name(artistDTO.getArtistName())
                    .comparableValue(artistDTO.getLeadStreams())
                    .comparableValueLabel("streams")
                    .imageUrl(artistDTO.getImageUrl())
                    .build();
            baseGameModelDTOS.add(baseGameModelDTO);
        }
        return baseGameModelDTOS;
    }
    private List<BaseGameModelDTO> convertSongDTOStoBaseGameModelDTO(String genre) {
        List<BaseGameModelDTO> baseGameModelDTOS = new ArrayList<>();
        List<SongDTO> songDTOS = spotifyController.getTop200SongsGenre(genre);
        for(SongDTO songDTO : songDTOS) {
            BaseGameModelDTO baseGameModelDTO = BaseGameModelDTO.builder()
                    .name(songDTO.getSongName() + " - " + songDTO.getArtistName())
                    .comparableValue(songDTO.getTotalStreams())
                    .comparableValueLabel("streams")
                    .imageUrl(songDTO.getImageUrl())
                    .build();
            baseGameModelDTOS.add(baseGameModelDTO);
        }
        return baseGameModelDTOS;
    }
}
