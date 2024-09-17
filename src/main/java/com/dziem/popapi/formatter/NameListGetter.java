package com.dziem.popapi.formatter;

import com.dziem.popapi.model.Artist;
import com.dziem.popapi.model.Song;
import com.dziem.popapi.repository.ArtistRepository;
import com.dziem.popapi.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.sql.Date;

@Component
@RequiredArgsConstructor
public class NameListGetter {
    private final ArtistRepository artistRepository;
    private final SongRepository songRepository;
    public void getList() {
        List<Date> lastUpdatesDate = artistRepository.findAllDates();
        List<LocalDate> lastUpdates = new ArrayList<>();
        for(Date lastUpdateDate : lastUpdatesDate) {
            lastUpdates.add(lastUpdateDate.toLocalDate());
        }
        LocalDate mostRecentUpdate = LocalDate.MIN;
        LocalDate secondMostRecentUpdate = LocalDate.MIN;

        for (LocalDate lastUpdate : lastUpdates) {
            if (lastUpdate.isAfter(mostRecentUpdate)) {
                secondMostRecentUpdate = mostRecentUpdate;
                mostRecentUpdate = lastUpdate;
            } else if (lastUpdate.isAfter(secondMostRecentUpdate) && !lastUpdate.equals(mostRecentUpdate)) {
                secondMostRecentUpdate = lastUpdate;
            }
        }
        List<Artist> mostRecentArtists = artistRepository.findAllArtistFromCertainUpdate(mostRecentUpdate);
        List<Artist> previousRecentArtists = artistRepository.findAllArtistFromCertainUpdate(secondMostRecentUpdate);
        HashMap<String, Artist> artistsHashMapMostRecent = new HashMap<>();
//        List<String> artistsNamesMostRecent = new ArrayList<>();
//        List<String> artistsNamesPrevious = new ArrayList<>();
        HashMap<String, Artist> artistsHashMapPrevious = new HashMap<>();
        for(int i = 0; i < 200; i++) {
//            artistsNamesMostRecent.add(mostRecentArtists.get(i).getArtistName());
//            artistsNamesPrevious.add(previousRecentArtists.get(i).getArtistName());
            artistsHashMapMostRecent.put(mostRecentArtists.get(i).getArtistName(), mostRecentArtists.get(i));
            artistsHashMapPrevious.put(previousRecentArtists.get(i).getArtistName(), previousRecentArtists.get(i));
        }
        List<String> missingArtistNames = new ArrayList<>();
        List<String> notLongerUsedArtistsImages = new ArrayList<>();
        List<String> artistsNamesMostRecent = artistsHashMapMostRecent.keySet().stream().toList();
        List<String> artistsNamesPrevious = artistsHashMapPrevious.keySet().stream().toList();
        for(int i = 0; i < 200; i++) {
            if(!artistsNamesPrevious.contains(artistsNamesMostRecent.get(i))) {
                missingArtistNames.add(artistsNamesMostRecent.get(i));
            }
            if (!artistsNamesMostRecent.contains(artistsNamesPrevious.get(i))) {
                notLongerUsedArtistsImages.add(artistsHashMapPrevious.get(artistsNamesPrevious.get(i)).getImageUrl());
            }
        }
        try (FileWriter myWriter = new FileWriter("src/main/resources/data/spotifyArtistsInFormatSuitableToExtractImages.txt", false)){
            for (String missingArtistName : missingArtistNames) {
                myWriter.write(missingArtistName + "\n");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        try (FileWriter myWriter = new FileWriter("src/main/resources/data/spotifyArtistsNoLongerUsedImages.txt", false)){
            for (String notLongerNeededArtistName : notLongerUsedArtistsImages) {
                myWriter.write(notLongerNeededArtistName + "\n");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
//        src/main/resources/spotifyArtistsInFormatSuitableToExtractImages.txt

    }
    public void getListSongs() {
        List<Song> songs = songRepository.findAll();
        List<String> songNames = new ArrayList<>();
        for(Song song : songs) {
            songNames.add(song.getSongName() + ";" + song.getArtistName());
        }
        try (FileWriter myWriter = new FileWriter("src/main/resources/data/spotifySongsInFormatSuitableToExtractImages.txt", false)){
            for (String songName : songNames) {
                myWriter.write(songName + "\n");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
