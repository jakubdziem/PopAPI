package com.dziem.popapi.controller;

import com.dziem.popapi.model.*;
import com.dziem.popapi.service.ModeStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
@RequiredArgsConstructor
@RestController
public class ModeController {
    private final ModeStatsService modeStatsService;
    @GetMapping("/api/v1/mode/{gameMode}")
    public List<BaseGameModelDTO> getElementsByGameMode(@PathVariable Mode gameMode) {
        return switch(gameMode) {
            case Mode.COUNTRIES_1900 -> modeStatsService.convertCountryDTOtoBaseGameModelDTO("1900", false);
            case Mode.COUNTRIES_1939 -> modeStatsService.convertCountryDTOtoBaseGameModelDTO("1939", false);
            case Mode.COUNTRIES_1989 -> modeStatsService.convertCountryDTOtoBaseGameModelDTO("1989", false);
            case Mode.COUNTRIES_NORMAL -> modeStatsService.convertCountryDTOtoBaseGameModelDTO(String.valueOf(LocalDate.now().getYear()),false);
            case Mode.COUNTRIES_FUTURE -> modeStatsService.convertCountryDTOtoBaseGameModelDTO(String.valueOf(LocalDate.now().getYear() + 100), false);
            case Mode.COUNTRIES_CHAOS -> modeStatsService.convertCountryDTOtoBaseGameModelDTO("", true);
            case Mode.SPOTIFY_TOP_ARTISTS -> modeStatsService.convertArtistDTOStoBaseGameModelDTO();
            case Mode.SPOTIFY_TOP_SONGS_GENERAL -> modeStatsService.convertSongDTOStoBaseGameModelDTO("General");
            case Mode.SPOTIFY_TOP_SONGS_POP -> modeStatsService.convertSongDTOStoBaseGameModelDTO("Pop");
            case Mode.SPOTIFY_TOP_SONGS_HIP_HOP -> modeStatsService.convertSongDTOStoBaseGameModelDTO("HipHop");
            case Mode.SPOTIFY_TOP_SONGS_ROCK -> modeStatsService.convertSongDTOStoBaseGameModelDTO("Rock");
            case Mode.FORMULA_TOP_SCORE -> modeStatsService.convertFormulaToBaseGameModelDTO();
            case Mode.APARTMENTS_WORLD -> modeStatsService.convertApartmentToBaseGameModelDTO("World", "$ per m²");
            case Mode.APARTMENTS_POLAND -> modeStatsService.convertApartmentToBaseGameModelDTO("Poland", "zł per m²");
            case Mode.HISTORY -> modeStatsService.convertHistoryToBaseGameModelDTO();
            case Mode.SOCIAL_MEDIA_TWITTER -> modeStatsService.convertSocialMediaToBaseGameModelDTO("Twitter");
            case Mode.SOCIAL_MEDIA_INSTAGRAM -> modeStatsService.convertSocialMediaToBaseGameModelDTO("Instagram");
            case Mode.SOCIAL_MEDIA_TIK_TOK -> modeStatsService.convertSocialMediaToBaseGameModelDTO("Tik tok");
            case Mode.SOCIAL_MEDIA_YOUTUBE -> modeStatsService.convertSocialMediaToBaseGameModelDTO("Youtube");
            case Mode.MOVIES -> modeStatsService.convertCinemaToBaseGameModelDTO("Movies");
            case Mode.TV_SHOWS -> modeStatsService.convertCinemaToBaseGameModelDTO("TV Shows");
            case Mode.CELEBS -> modeStatsService.convertCinemaToBaseGameModelDTO("Celebs");
            case Mode.FORMULA_TOP_DRIVERS_PODIUMS -> modeStatsService.convertDriverPodiumsToBaseGameModelDTO();
            case Mode.FORMULA_TOP_DRIVERS_FASTEST_LAPS -> modeStatsService.convertDriverFastestLapsToBaseGameModelDTO();
            case Mode.FORMULA_TOP_DRIVERS_GP -> modeStatsService.convertDriverGPToBaseGameModelDTO();
            case Mode.FORMULA_TOP_COUNTRIES_GP -> modeStatsService.convertCountryGPToBaseGameModelDTO();
            case Mode.FORMULA_TOP_TEAMS_POINTS -> modeStatsService.convertTeamsPointsToBaseGameModelDTO();
            case Mode.FORMULA_TOP_TEAMS_GP -> modeStatsService.convertTeamsGPToBaseGameModelDTO();
        };
    }
}
