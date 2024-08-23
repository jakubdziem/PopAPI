package com.dziem.popapi.service;

import com.dziem.popapi.model.User;
import com.dziem.popapi.model.UName;
import com.dziem.popapi.repository.UNameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public UName initializeUserName(User user) {
        return uNameRepository.save(UName.builder()
                .user(user)
                .lastUpdate(LocalDateTime.now())
                .name("Not assigned")
                .build());

    }
}
