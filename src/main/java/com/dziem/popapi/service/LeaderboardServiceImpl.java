package com.dziem.popapi.service;

import com.dziem.popapi.mapper.LeaderboardMapper;
import com.dziem.popapi.model.*;
import com.dziem.popapi.repository.LeaderboardRepository;
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

}
