package com.dziem.popapi.service;

import com.dziem.popapi.mapper.LeaderboardMapper;
import com.dziem.popapi.model.*;
import com.dziem.popapi.repository.LeaderboardRepository;
import com.dziem.popapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaderboardServiceImpl implements LeaderboardService {
    private final LeaderboardRepository leaderboardRepository;
    private final LeaderboardMapper leaderboardMapper;
    private final UserRepository userRepository;
    @Override
    public List<LeaderboardDTO> getLeaderboard(String mode) {
        List<Leaderboard> leaderboardList = leaderboardRepository.findAll().stream().filter(leaderboard -> leaderboard.getMode().equals(mode)).toList();
        List<LeaderboardDTO> leaderboardDTOList = new ArrayList<>();
        for(Leaderboard leaderboard : leaderboardList) {
            LeaderboardDTO leaderboardDTO = leaderboardMapper.leaderboardtoLeaderboardDTO(leaderboard);
            leaderboardDTOList.add(leaderboardDTO);
        }
        return leaderboardDTOList.stream().sorted(Comparator.comparingInt(LeaderboardDTO::getScore).reversed()).toList();
    }

    @Override
    public List<Leaderboard> initializeLeaderboard(String userId, User user) {
        List<Leaderboard> leaderboardList = new ArrayList<>();
        for(Mode mode : Mode.values()) {
            Leaderboard leaderboard = Leaderboard.builder()
                    .user(user)
                    .mode(mode.toString())
                    .score(0)
                    .build();
            leaderboardList.add(leaderboard);
            leaderboardRepository.save(leaderboard);
        }
        return leaderboardList;
    }

    @Override
    public Integer getRankOfUserInMode(String userId, String mode) {
        if(userRepository.existsById(userId)) {
            Comparator<Leaderboard> comparator = (o1, o2) -> {
                if (o1.getScore().equals(o2.getScore())) {
                    return 0;
                }
                return o1.getScore().compareTo(o2.getScore());
            };
            List<Leaderboard> leaderboardSingle = leaderboardRepository.findAll().stream().filter(leaderboard -> leaderboard.getUser().getUserId().equals(userId) && leaderboard.getMode().equals(mode)).toList();
            List<Leaderboard> leaderboardList = leaderboardRepository.findAll().stream()
                    .filter(leaderboard -> leaderboard.getMode().equals(mode)).sorted(comparator.reversed()).toList();
            return leaderboardList.indexOf(leaderboardSingle.get(0)) + 1;
        } else {
            return -1;
        }
    }

}
