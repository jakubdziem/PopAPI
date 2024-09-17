package com.dziem.popapi.formatter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class SpotifyTopArtistDataFormatter {
    public static void main(String[] args) {
//        System.out.println(formatSpotifyFile("src/main/resources/data/artist.txt"));
        System.out.println(formatSpotifyFile("src/main/resources/data/artist.txt"));
    }
    public static String formatSpotifyFile(String path) {
        AtomicReference<String> res = new AtomicReference<>("");
        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            stream.forEach(line -> res.set(res.get().concat(line).concat("\n")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res.get();
    }
}
