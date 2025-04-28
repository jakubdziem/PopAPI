package com.dziem.popapi.mapper;

import com.dziem.popapi.model.Leaderboard;
import com.dziem.popapi.dto.LeaderboardDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface LeaderboardMapper {
    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "user.UName.name", target = "name")
    LeaderboardDTO leaderboardtoLeaderboardDTO(Leaderboard leaderboard);
}
