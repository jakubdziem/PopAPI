package com.dziem.popapi.service;

import com.dziem.popapi.model.User;
import com.dziem.popapi.model.UName;
import com.dziem.popapi.repository.UNameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
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
    };
    @Override
    public String generateRandomUserName() {
        long count = uNameRepository.count();
        Random random = new Random();
        int min = 0;
        int max = 7;
        return exampleNames[random.nextInt(max - min + 1) + min] + count;
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
