package com.dziem.popapi.service;

import com.dziem.popapi.mapper.ModeStatsMapper;
import com.dziem.popapi.model.*;
import com.dziem.popapi.repository.ModeStatsRepository;
import com.dziem.popapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ModeStatsServiceImpl implements ModeStatsService {
    private final ModeStatsRepository modeStatsRepository;
    private final ModeStatsMapper modeStatsMapper;
    private final UserRepository userRepository;
    @Override
    public ModeStats initializeModeStats(User user, String mode) {
        ModeStats modeStats = new ModeStats();
        modeStats.setUser(user);
        modeStats.setTotalGamePlayed(0L);
        modeStats.setTimePlayed(0L);
        modeStats.setAvgScore(new BigDecimal("0.0"));
        modeStats.setTotalScoredPoints(0L);
        modeStats.setNumberOfWonGames(0);
        modeStats.setMode(mode);
        return modeStatsRepository.save(modeStats);
    }

    @Override
    public boolean updateStatistics(String uuid, String stats) {
        String[] split = stats.split(",");
        List<ModeStats> modeStatsList = modeStatsRepository.findAll().stream().filter(modeStats -> modeStats.getUser().getUserId().equals(uuid) && modeStats.getMode().equals(split[3])).toList();
        if(!modeStatsList.isEmpty()) {
            ModeStats modeStatsExisting = modeStatsList.get(0);
            modeStatsExisting.setTotalGamePlayed(modeStatsExisting.getTotalGamePlayed()+1L);
            modeStatsExisting.setTimePlayed(modeStatsExisting.getTimePlayed()+Long.parseLong(split[0]));
            long scoredPoints = Long.parseLong(split[1]);
            modeStatsExisting.setTotalScoredPoints(modeStatsExisting.getTotalScoredPoints()+ scoredPoints);
            if(split[2].equals("y")) {
                modeStatsExisting.setNumberOfWonGames(modeStatsExisting.getNumberOfWonGames() + 1);
                //tez w wonGames table zaaktualizowac jak juz sie pojawi ta tablea
            }
            modeStatsExisting.setAvgScore(StatsServiceImpl.calculateAvgScore(modeStatsExisting.getTotalScoredPoints(), modeStatsExisting.getTotalGamePlayed()));
            modeStatsRepository.save(modeStatsExisting);
            return true;
        }
        return false;
    }

    @Override
    public List<ModeStatsDTO> getStatsByUserId(String userId) {
        List<ModeStats> modeStatsList = modeStatsRepository.findAll().stream().filter(modeStats -> modeStats.getUser().getUserId().equals(userId)).toList();
        List<ModeStatsDTO> modeStatsDTOS = new ArrayList<>();
        for(ModeStats modeStats : modeStatsList) {
            modeStatsDTOS.add(modeStatsMapper.modeStatsToModeStatsDTO(modeStats));
        }
        return modeStatsDTOS;
    }

    @Override
    public boolean updateStatisticsMultipleInput(String userId, List<GameStatsDTO> gameStatsDTOS) {
        if(userRepository.findById(userId).isPresent()) {
            for(GameStatsDTO gameStatsDTO : gameStatsDTOS) {
                String stats = gameStatsDTO.getTimePlayedSeconds().toString().concat(",")
                        .concat(gameStatsDTO.getScoredPoints().toString()).concat(",")
                        .concat(gameStatsDTO.isWonGame() ? "y" : "n").concat(",")
                        .concat(gameStatsDTO.getGameMode());
                updateStatistics(userId, stats);
            }
            return true;
        }
        return false;
    }
}
