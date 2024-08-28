package com.dziem.popapi.formatter;

import com.dziem.popapi.service.DataServiceImpl;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

@Component
public class FirstLineOfFormatSongsFiles {
    public static void formatSpotifyFile(String pathFromUser) {
        AtomicReference<String> res = new AtomicReference<>("");
        try (Stream<String> stream = Files.lines(Path.of(pathFromUser))) {
            stream.forEach(line -> {
                String convertedLine = line
                        .replace("\t", " ")
                        .replace("  ", " ");
                res.set(res.get().concat(convertedLine).concat("\n"));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        try(FileWriter fileWriter = new FileWriter(pathFromUser, false)) {
            int i = 0;
            String format = LocalDate.now().format(DataServiceImpl.FORMATTER);
            String s = res.get().split("\n")[0];
            for(Character character : s.toCharArray()) {
                if(character == '.') {
                    i++;
                }
            }
            if(i == 2 && s.length() == format.length()) {
                List<String> list = new java.util.ArrayList<>(Arrays.stream(res.get().split("\n")).toList());
                list.remove(0);
                String result = "";
                for(String line : list) {
                    result = result.concat(line).concat("\n");
                }
                res.set(result);
                fileWriter.write(format.concat("\n"));
            } else {
                fileWriter.write(format.concat("\n"));
            }
            fileWriter.write(res.get());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
