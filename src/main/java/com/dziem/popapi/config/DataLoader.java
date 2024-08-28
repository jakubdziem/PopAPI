package com.dziem.popapi.config;

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
//        nameListGetter.getList();
//        System.out.println("DataLoader finished execution.");
//        dataService.getDataSpotifyTopSongs(""); //need to populate postgresql
//        dataService.getDataSpotifyTopSongs("HipHop");
//        dataService.getDataSpotifyTopSongs("Rock");
//        dataService.getDataSpotifyTopSongs("Pop");
//        nameListGetter.getListSongs();
    }
}