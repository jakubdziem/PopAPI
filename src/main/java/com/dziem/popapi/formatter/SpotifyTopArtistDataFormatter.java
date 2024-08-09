package com.dziem.popapi.formatter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class SpotifyTopArtistDataFormatter {
    public static void main(String[] args) {
        System.out.println(formatSpotifyFile("src/main/resources/spotifyTopArtistData.txt"));
    }
    public static String formatSpotifyFile(String path) {
        AtomicReference<String> res = new AtomicReference<>("");
        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            stream.forEach(line -> {
                res.set(res.get().concat(line).concat("\n"));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] strip = res.get().split("\n");
        String formattedString = "";
        for(int i = 0; i < strip.length; i++) {
            if(i==0) {
                formattedString = formattedString.concat(strip[i]).concat("\n");
            } else {
                formattedString = formattedString.concat(strip[i]).concat(i % 2 == 1 ? ";" : "\n");
            }
        }
        return formattedString;
    }
}
