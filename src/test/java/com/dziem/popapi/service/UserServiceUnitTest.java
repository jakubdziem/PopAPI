package com.dziem.popapi.service;

import com.dziem.popapi.model.*;
import com.dziem.popapi.repository.LeaderboardRepository;
import com.dziem.popapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private StatsService statsService;

    @Mock
    private ScoreService scoreService;

    @Mock
    private UNameService uNameService;

    @Mock
    private LeaderboardRepository leaderboardRepository;

    @Mock
    private LeaderboardService leaderboardService;

    @Mock
    private ModeStatsService modeStatsService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
    }
    @Test
    void shouldGenerateValidUUID() {
        String generatedUserId = userService.generateUniqueUUID();
        System.out.println(generatedUserId);
        assertThat(generatedUserId).isNotEmpty();
        assertThat(generatedUserId.length()).isEqualTo(UUID.randomUUID().toString().length());
        assertThat(UUID.fromString(generatedUserId)).isNotNull();
    }

    @Test
    void shouldCreateAnonimUserSuccessfully() {
        boolean guest = true;
        assertThatUserInitializationIsCorrect(guest);
    }
    @Test
    void shouldCreateGoogleUserSuccessfully() {
        boolean guest = false;
        assertThatUserInitializationIsCorrect(guest);
    }

    private void assertThatUserInitializationIsCorrect(boolean guest) {
        String generatedUserId = userService.generateUniqueUUID();
        when(statsService.initializeStats(any(User.class))).thenReturn(new Stats());
        Set<Mode> modeSet = new HashSet<>(Arrays.asList(Mode.values()));
        for(Mode mode : modeSet) {
            String modeStr = mode.toString();
            when(scoreService.initializeScore(eq(modeStr), any(User.class))).thenAnswer(invocationOnMock ->  {
                Score score = new Score();
                score.setMode(invocationOnMock.getArgument(0));
                return score;
            });
            when(modeStatsService.initializeModeStats(any(User.class), eq(modeStr))).thenAnswer(invocationOnMock -> {
                ModeStats modeStats = new ModeStats();
                modeStats.setMode(invocationOnMock.getArgument(1));
                return modeStats;
            });
        }

        String setUsername = "RandomUsername";
        when(uNameService.generateRandomUsername()).thenReturn(setUsername);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setUserId(generatedUserId);
            return user;
        });

        User user = userService.createUser(generatedUserId, guest);
        assertThat(user).isNotNull();
        assertThat(user.isGuest()).isEqualTo(guest);
        assertThat(user.getStatistics()).isNotNull();

        assertThat(user.getBestScores()).isNotNull();
        assertThat(user.getBestScores().size()).isEqualTo(modeSet.size());

        for(Score score : user.getBestScores()) {
            assertThat(score).isNotNull();
            assertThat(modeSet.contains(Mode.valueOf(score.getMode()))).isTrue();
        }

        assertThat(user.getModeStats()).isNotNull();
        assertThat(user.getModeStats().size()).isEqualTo(modeSet.size());

        for(ModeStats modeStats : user.getModeStats()) {
            assertThat(modeStats).isNotNull();
            assertThat(modeSet.contains(Mode.valueOf(modeStats.getMode()))).isTrue();
        }

        assertThat(user.getUName()).isNotNull();
        assertThat(user.getUName().getName()).isEqualTo(setUsername);
        assertThat(user.getUName().getUser()).isEqualTo(user);
        assertThat(user.getUName().getLastUpdate()).isEqualTo(LocalDateTime.of(2000,1,1,0,0,0,0));
    }

}
