package com.dziem.popapi.service;

import com.dziem.popapi.mapper.LeaderboardMapper;
import com.dziem.popapi.model.*;
import com.dziem.popapi.repository.LeaderboardRepository;
import com.dziem.popapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class LeaderboardServiceImpl implements LeaderboardService {
    private final LeaderboardRepository leaderboardRepository;
    private final LeaderboardMapper leaderboardMapper;
    private final UserRepository userRepository;
    @Override
    public ResponseEntity<List<LeaderboardDTO>> getLeaderboardFirst200(String mode) {
        return new ResponseEntity<>(getLeaderboard(mode).stream().limit(200).toList(), HttpStatus.ACCEPTED);
    }

    @Override
    public List<Leaderboard> initializeLeaderboard(String userId, User user) {
        List<Leaderboard> leaderboardList = new ArrayList<>();
        for(Mode mode : Mode.values()) {
            Leaderboard leaderboard = Leaderboard.builder()
                    .user(user)
                    .mode(mode.toString())
                    .score(0)
                    .name(user.getUName().getName())
                    .build();
            leaderboardList.add(leaderboard);
            leaderboardRepository.save(leaderboard);
        }
        return leaderboardList;
    }

    @Override
    public Optional<RankScoreDTO> getRankOfUserInMode(String userId, String mode) {
        AtomicReference<Optional<RankScoreDTO>> atomicReference = new AtomicReference<>();
        boolean modeExisting = isModeExisting(mode);
        if(modeExisting) {
            userRepository.findById(userId).ifPresentOrElse(existing -> {
                if (existing.isGuest()) {
                    atomicReference.set(Optional.empty());
                } else {
                    List<LeaderboardDTO> leaderboard = getLeaderboard(mode);
                    int rank = 0;
                    int score = 0;
                    for (int i = 0; i < (leaderboard != null ? leaderboard.size() : 0); i++) {
                        if (leaderboard.get(i).getUserId().equals(userId)) {
                            rank = i + 1;
                            score = leaderboard.get(i).getScore();
                            break;
                        }
                    }
                    RankScoreDTO rankScoreDTO = new RankScoreDTO();
                    rankScoreDTO.setRank(rank);
                    rankScoreDTO.setScore(score);
                    atomicReference.set(Optional.of(rankScoreDTO));
                }
            }, () -> atomicReference.set(Optional.empty()));
            return atomicReference.get();
        }
        else {
            return Optional.empty();
        }
    }

    private List<LeaderboardDTO> getLeaderboard(String mode) {
        boolean modeExisting = isModeExisting(mode);
        if(!modeExisting) {
            return new ArrayList<>();
        }
        List<Leaderboard> leaderboardList = leaderboardRepository.findAll().stream().filter(leaderboard -> leaderboard.getMode().equals(mode)).toList();
        List<LeaderboardDTO> leaderboardDTOList = new ArrayList<>();
        for(Leaderboard leaderboard : leaderboardList) {
            LeaderboardDTO leaderboardDTO = leaderboardMapper.leaderboardtoLeaderboardDTO(leaderboard);
            leaderboardDTOList.add(leaderboardDTO);
        }
        return leaderboardDTOList.stream().sorted((o1, o2) -> (o1.compareTo(o2) == 0 ? (o1.getId() < o2.getId() ? -1 : 1) : o1.compareTo(o2))).toList();
        //it's reversed, because I couldn't find using reversed() in lambda
    }


    private static boolean isModeExisting(String mode) {
        boolean modeExisting = false;
        for(Mode defaultMode : Mode.values()) {
            if(defaultMode.toString().equals(mode)) {
                modeExisting = true;
                break;
            }
        }
        return modeExisting;
    }

}
