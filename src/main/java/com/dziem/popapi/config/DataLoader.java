package com.dziem.popapi.config;

import com.dziem.popapi.formatter.ArtistNameListGetter;
import com.dziem.popapi.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final DataService dataService;
    private final ArtistNameListGetter artistNameListGetter;
    @Override
    public void run(String... args) throws Exception {
        dataService.getData2024_2100();
        dataService.getData2100_2150();
        dataService.getDataSpotifyTopArtists();
        artistNameListGetter.getList();
    }
}