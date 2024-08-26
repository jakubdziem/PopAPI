package com.dziem.popapi.service;

import com.dziem.popapi.model.User;
import com.dziem.popapi.model.UName;
import com.dziem.popapi.repository.UNameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class UNameServiceImpl implements UNameService {
    private final UNameRepository uNameRepository;
    private final String[] exampleNames = {
            "CountryGuesser",
            "PopGuesser",
            "GlobalPopExpert",
            "ModernPopulationGuru",
            "FuturePopPredictor",
            "FuturisticPopMaster",
            "TopTunesMaster",
            "HitSpotter",
            "PopMastermind",
            "TuneTracker",
            "TuneTracker",
            "HitHunter",
            "HitScout",
            "NationExplorer",
            "CountryDetective",
            "NationNerd",
            "SongSpotter",
            "SoundSniper",
            "MusicMastermind",
            "ArtistHunter",
            "ArtistScout"
    };
    @Override
    public String generateRandomUserName() {
        long count = uNameRepository.count();
        Random random = new Random();
        int min = 0;
        int max = exampleNames.length-1;
        return exampleNames[random.nextInt(max - min + 1) + min] + count;
    }
    @Override
    public boolean setUserName(String userId, String name) {
        AtomicReference<Boolean> atomicReference = new AtomicReference<>();
        uNameRepository.findById(userId).ifPresentOrElse(
                uName -> {
                    atomicReference.set(true);
                    uName.setName(name);
                    uName.setLastUpdate(LocalDateTime.now());
                    uNameRepository.save(uName);
                },
                () -> atomicReference.set(false)
        );

        return atomicReference.get();
    }
    @Override
    public boolean validateUserName(String name) {
        if(name.length() < 3 || name.length() > 20) {
            return false;
        }
        AtomicReference<Boolean> atomicReference = new AtomicReference<>(false);
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("src/main/resources/restricted_words.txt"))) {
            List<String> list = reader.lines().toList();
            for(int i = 3; i <= name.length(); i++) {
                for(int j = 0; j < i; j++) {
                    if(list.contains(name.substring(j,i).toLowerCase())) {
                        atomicReference.set(true);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return !atomicReference.get();
    }

    @Override
    public UName initializeUserName(User user) {
        return uNameRepository.save(UName.builder()
                .user(user)
                .lastUpdate(LocalDateTime.now())
                .name("Not assigned")
                .build());

    }
}
