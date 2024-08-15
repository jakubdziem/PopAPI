package com.dziem.popapi.formatter;

import com.dziem.popapi.model.Artist;
import com.dziem.popapi.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Component
@RequiredArgsConstructor
public class ArtistNameListGetter {
    private final ArtistRepository artistRepository;
    public void getList() {
        List<Artist> artists = artistRepository.findAll();
        List<String> artistsNames = new ArrayList<>();
        for(Artist artist : artists) {
            artistsNames.add(artist.getArtistName());
        }
        try {
            FileWriter myWriter = new FileWriter("src/main/resources/spotifyArtistsInFormatSuitableToExtractImages.txt");
            for(int i = 0; i < artistsNames.size(); i++) {
                myWriter.write(artistsNames.get(i)+"\n");
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
//        src/main/resources/spotifyArtistsInFormatSuitableToExtractImages.txt

    }

}
