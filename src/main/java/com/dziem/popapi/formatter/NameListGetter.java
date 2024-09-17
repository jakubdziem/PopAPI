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
        TwoMostRecentUpdates twoMostRecentUpdates = getResult(lastUpdatesDate);
        List<Artist> mostRecentArtists = artistRepository.findAllArtistsFromCertainUpdate(twoMostRecentUpdates.mostRecentUpdate());
        List<Artist> previousRecentArtists = artistRepository.findAllArtistsFromCertainUpdate(twoMostRecentUpdates.secondMostRecentUpdate());
        HashMap<String, String> artistsHashMapMostRecent = new HashMap<>();
        HashMap<String, String> artistsHashMapPrevious = new HashMap<>();
        for(int i = 0; i < 200; i++) {
            Artist mostRecentArtist = mostRecentArtists.get(i);
            artistsHashMapMostRecent.put(mostRecentArtist.getArtistName(), mostRecentArtist.getImageUrl());
            Artist previousArtist = previousRecentArtists.get(i);
            artistsHashMapPrevious.put(previousArtist.getArtistName(), previousArtist.getImageUrl());
        }
        List<String> missingArtistNames = new ArrayList<>();
        List<String> notLongerUsedArtistsImagesUrls = new ArrayList<>();
        List<String> artistsNamesMostRecent = artistsHashMapMostRecent.keySet().stream().toList();
        List<String> artistsNamesPrevious = artistsHashMapPrevious.keySet().stream().toList();
        for(int i = 0; i < 200; i++) {
            if(!artistsNamesPrevious.contains(artistsNamesMostRecent.get(i))) {
                missingArtistNames.add(artistsNamesMostRecent.get(i));
            }
            if (!artistsNamesMostRecent.contains(artistsNamesPrevious.get(i))) {
                notLongerUsedArtistsImagesUrls.add(artistsHashMapPrevious.get(artistsNamesPrevious.get(i)));
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
            for (String notLongerNeededArtistImageUrl : notLongerUsedArtistsImagesUrls) {
                myWriter.write(notLongerNeededArtistImageUrl + "\n");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
//        src/main/resources/spotifyArtistsInFormatSuitableToExtractImages.txt

    }

    public void getListSongs() {
        List<Date> lastUpdatesDate = songRepository.findAllDates();
        TwoMostRecentUpdates twoMostRecentUpdates = getResult(lastUpdatesDate);
        List<Song> mostRecentSongs= songRepository.findAllSongsFromCertainUpdate(twoMostRecentUpdates.mostRecentUpdate());
        List<Song> previousRecentSongs = songRepository.findAllSongsFromCertainUpdate(twoMostRecentUpdates.secondMostRecentUpdate());
        HashMap<String, String> songsHashMapMostRecent = new HashMap<>();
        HashMap<String, String> songsHashMapPrevious = new HashMap<>();
        for (Song mostRecentSong : mostRecentSongs) {
            songsHashMapMostRecent.put(mostRecentSong.getSongName().concat(";").concat(mostRecentSong.getArtistName()), mostRecentSong.getImageUrl());
        }
        for (Song previousSong : previousRecentSongs) {
            songsHashMapPrevious.put(previousSong.getSongName().concat(";").concat(previousSong.getArtistName()), previousSong.getImageUrl());
        }
        List<String> missingSongAndArtistsNames = new ArrayList<>();
        List<String> notLongerUsedSongsImagesUrls = new ArrayList<>();
        List<String> songAndArtistsNamesMostRecent = songsHashMapMostRecent.keySet().stream().toList();
        List<String> songAndArtistsNamesPrevious = songsHashMapPrevious.keySet().stream().toList();
        for (String s : songAndArtistsNamesMostRecent) {
            if (!songAndArtistsNamesPrevious.contains(s)) {
                missingSongAndArtistsNames.add(s);
            }
        }
        for (String andArtistsNamesPrevious : songAndArtistsNamesPrevious) {
            if (!songAndArtistsNamesMostRecent.contains(andArtistsNamesPrevious)) {
                notLongerUsedSongsImagesUrls.add(songsHashMapPrevious.get(andArtistsNamesPrevious));
            }
        }
        try (FileWriter myWriter = new FileWriter("src/main/resources/data/spotifySongsInFormatSuitableToExtractImages.txt", false)){
            for (String songAndArtistName : missingSongAndArtistsNames) {
                myWriter.write(songAndArtistName + "\n");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        try (FileWriter myWriter = new FileWriter("src/main/resources/data/spotifySongsNoLongerUsedImages.txt", false)){
            for (String notLongerNeededSongImageUrl : notLongerUsedSongsImagesUrls) {
                myWriter.write(notLongerNeededSongImageUrl + "\n");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static TwoMostRecentUpdates getResult(List<Date> lastUpdatesDate) {
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
        return new TwoMostRecentUpdates(mostRecentUpdate, secondMostRecentUpdate);
    }

    private record TwoMostRecentUpdates(LocalDate mostRecentUpdate, LocalDate secondMostRecentUpdate) {
    }

}
