package com.dziem.popapi.formatter;

import com.dziem.popapi.model.Artist;
import com.dziem.popapi.model.Song;
import com.dziem.popapi.repository.ArtistRepository;
import com.dziem.popapi.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Component
@RequiredArgsConstructor
public class NameListGetter {
    private final ArtistRepository artistRepository;
    private final SongRepository songRepository;
    public void getList() {
        List<Artist> artists = artistRepository.findAll();
        List<String> artistsNames = new ArrayList<>();
        for(Artist artist : artists) {
            artistsNames.add(artist.getArtistName());
        }
        try (FileWriter myWriter = new FileWriter("src/main/resources/spotifyArtistsInFormatSuitableToExtractImages.txt", false)){
            for(int i = 0; i < artistsNames.size(); i++) {
                myWriter.write(artistsNames.get(i)+"\n");
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
            for(int i = 0; i < songNames.size(); i++) {
                myWriter.write(songNames.get(i)+"\n");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
