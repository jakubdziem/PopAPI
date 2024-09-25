package com.dziem.popapi.service;

import com.dziem.popapi.model.*;

import java.util.List;

public interface ModeStatsService {
    ModeStats initializeModeStats(User user, String mode);
    boolean updateStatistics(String uuid, String stats);
    List<ModeStatsDTO> getStatsByUserId(String userId);
    boolean updateStatisticsMultipleInput(String userId, List<GameStatsDTO> gameStatsDTOS);
    List<BaseGameModelDTO> convertCountryDTOtoBaseGameModelDTO(String year, boolean chaos);
    List<BaseGameModelDTO> convertArtistDTOStoBaseGameModelDTO();
    List<BaseGameModelDTO> convertSongDTOStoBaseGameModelDTO(String genre);

    List<BaseGameModelDTO> convertFormulaToBaseGameModelDTO();

    List<BaseGameModelDTO> convertApartmentToBaseGameModelDTO(String country, String sign);

    List<BaseGameModelDTO> convertHistoryToBaseGameModelDTO();

    List<BaseGameModelDTO> convertSocialMediaToBaseGameModelDTO(String type);

    List<BaseGameModelDTO> convertCinemaToBaseGameModelDTO(String type);

}
