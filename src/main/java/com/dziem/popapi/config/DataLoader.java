package com.dziem.popapi.config;

import com.dziem.popapi.formatter.GetShortImageSource;
import com.dziem.popapi.formatter.NameListGetter;
import com.dziem.popapi.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;


@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final DataService dataService;
    private final NameListGetter nameListGetter;
    private final GetShortImageSource getShortImageSource;
    @Async
    public CompletableFuture<Void> asyncGetData2024_2100() {
        dataService.getData2024_2100();
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void run(String... args) throws Exception {
//        System.out.println("DataLoader is running...");
//        System.out.println("Data 2024_2100 is running...");
//        asyncGetData2024_2100().join(); // Wait for the async operation to complete
//        System.out.println("Data 2024_2100 completed.");
//        System.out.println("Data 2100_2150 is running.");
//        dataService.getData2100_2150();
//        System.out.println("Data 2100_2150 completed.");
//        System.out.println("Data spotify is running...");
//        dataService.getDataSpotifyTopArtists();
//        System.out.println("Data spotify completed.");
//        System.out.println("Start");
//        nameListGetter.getArtistList();
//        nameListGetter.getListSongs();
//        System.out.println("End");
//        System.out.println("DataLoader finished execution.");
//        dataService.getDataSpotifyTopSongs("General");
//        dataService.getDataSpotifyTopSongs("HipHop");
//        dataService.getDataSpotifyTopSongs("Rock");
//        dataService.getDataSpotifyTopSongs("Pop");
//        System.out.println("Start");
//        System.out.println("End");
//        dataService.addSourceToDriver();
//        dataService.addSourceApartmentsPoland();
//        dataService.addSourceHistory();
//        dataService.addSourceCountriesAndApartmentsWorld();
//        dataService.getDataSocialMedia();
//        dataService.addSourcesToSocialMedia();
//        System.out.println("Loading");
//        dataService.getCelebsData();
//        dataService.getTVShowsData();
//        dataService.getMovieData();
//        System.out.println("Loaded");
//        dataService.addSourcesToCinema("Celebs", "src/main/resources/imageSource/celebsSourcesOfPhotos.txt");
//        dataService.addSourcesToCinema("TV Shows", "src/main/resources/imageSource/tvShowsSourcesOfPhotos.txt");
//        dataService.addSourcesToCinema("Movies", "src/main/resources/imageSource/moviesSourcesOfPhotos.txt");
//        dataService.addSourcesToArtist("src/main/resources/imageSource/artistsSourcesOfPhotos.txt");
        getShortImageSource.printShortSource();
    }
}