package com.dziem.popapi.service;

import com.dziem.popapi.mapper.ModeStatsMapper;
import com.dziem.popapi.model.*;
import com.dziem.popapi.repository.HistoryRepository;
import com.dziem.popapi.repository.ModeStatsRepository;
import com.dziem.popapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ModeStatsServiceImpl implements ModeStatsService {
    private final ModeStatsRepository modeStatsRepository;
    private final CountryService countryService;
    private final ArtistService artistService;
    private final SongService songService;
    private final ModeStatsMapper modeStatsMapper;
    private final UserRepository userRepository;
    private final DriverService driverService;
    private final ApartmentService apartmentService;
    private final HistoryRepository historyRepository;
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

    @Override
    public List<BaseGameModelDTO> convertCountryDTOtoBaseGameModelDTO(String year, boolean chaos) {
        List<BaseGameModelDTO> baseGameModelDTOS = new ArrayList<>();
        List<CountryDTO> countryDTOS;
        if(chaos) {
            countryDTOS = countryService.findCountriesByDistinctYear("1900", true);
            countryDTOS.addAll(countryService.findCountriesByDistinctYear("1939", true));
            countryDTOS.addAll(countryService.findCountriesByDistinctYear("1989", true));
            int currentYear = LocalDate.now().getYear();
            countryDTOS.addAll(countryService.findCountriesByDistinctYear(String.valueOf(currentYear), true));
            countryDTOS.addAll(countryService.findCountriesByDistinctYear(String.valueOf(currentYear + 100), true));
        } else {
            countryDTOS = countryService.findCountriesByDistinctYear(year, false);
        }
        for(CountryDTO countryDTO : countryDTOS) {
            BaseGameModelDTO baseGameModelDTOCountry = BaseGameModelDTO.builder()
                    .name(countryDTO.getCountryName())
                    .comparableValue(Float.parseFloat(countryDTO.getYearAndPopulations().getFirst().getPopulation()))
                    .comparableValueLabel("population")
                    .imageUrl(countryDTO.getFlagUrl())
                    .tier(countryDTO.getYearAndPopulations().getFirst().getTier())
                    //countryDTO.getYearAndPopulations().getFirst().getTier() == null ? 1 : countryDTO.getYearAndPopulations().getFirst().getTier()
                    .build();
            baseGameModelDTOS.add(baseGameModelDTOCountry);
        }
        return baseGameModelDTOS;
    }

    @Override
    public List<BaseGameModelDTO> convertArtistDTOStoBaseGameModelDTO() {
        List<BaseGameModelDTO> baseGameModelDTOS = new ArrayList<>();
        List<ArtistDTO> artistDTOS = artistService.getTop200Artists();
        for(int i = 0; i < artistDTOS.size(); i++) {
            BaseGameModelDTO baseGameModelDTO = BaseGameModelDTO.builder()
                    .name(artistDTOS.get(i).getArtistName())
                    .comparableValue(Float.parseFloat(artistDTOS.get(i).getLeadStreams().replace(",","")))
                    .comparableValueLabel("streams")
                    .imageUrl(artistDTOS.get(i).getImageUrl())
                    .tier(i < 50 ? 1 : 2)
                    .build();
            baseGameModelDTOS.add(baseGameModelDTO);
        }
        return baseGameModelDTOS;
    }

    @Override
    public List<BaseGameModelDTO> convertSongDTOStoBaseGameModelDTO(String genre) {
        List<BaseGameModelDTO> baseGameModelDTOS = new ArrayList<>();
        List<SongDTO> songDTOS = songService.getTop200SongsGenre(genre);
        for(int i = 0; i < songDTOS.size(); i++) {
            BaseGameModelDTO baseGameModelDTO = BaseGameModelDTO.builder()
                    .name(songDTOS.get(i).getSongName() + " - " + songDTOS.get(i).getArtistName())
                    .comparableValue(Float.parseFloat(songDTOS.get(i).getTotalStreams().replace(",","")))
                    .comparableValueLabel("streams")
                    .imageUrl(songDTOS.get(i).getImageUrl())
                    .tier(i < 50 ? 1 : 2)
                    .build();
            baseGameModelDTOS.add(baseGameModelDTO);
        }
        return baseGameModelDTOS;
    }

    @Override
    public List<BaseGameModelDTO> convertFormulaToBaseGameModelDTO() {
        List<BaseGameModelDTO> baseGameModelDTOS = new ArrayList<>();
        List<Driver> topScoreDrivers = driverService.getTopScoreDrivers();
        for(Driver driver : topScoreDrivers) {
            BaseGameModelDTO baseGameModelDTO = BaseGameModelDTO.builder()
                    .name(driver.getName())
                    .comparableValue(driver.getScore())
                    .comparableValueLabel("points")
                    .imageUrl(driver.getImageUrl())
                    .tier(driver.getTier())
                    .build();
            baseGameModelDTOS.add(baseGameModelDTO);
        }
        return baseGameModelDTOS;
    }

    @Override
    public List<BaseGameModelDTO> convertApartmentToBaseGameModelDTO(String country, String sign) {
        List<BaseGameModelDTO> baseGameModelDTOS = new ArrayList<>();
        List<Apartment> topPricesApartments = apartmentService.getApartments(country);
        for(Apartment apartment : topPricesApartments) {
            BaseGameModelDTO baseGameModelDTO = BaseGameModelDTO.builder()
                    .name(apartment.getCountryOrCityName())
                    .comparableValue(apartment.getPrice())
                    .comparableValueLabel(sign)
                    .imageUrl(apartment.getImageUrl())
                    .tier(apartment.getTier())
                    .build();
            baseGameModelDTOS.add(baseGameModelDTO);
        }
        return baseGameModelDTOS;
    }

    @Override
    public List<BaseGameModelDTO> convertHistoryToBaseGameModelDTO() {
        List<BaseGameModelDTO> baseGameModelDTOS = new ArrayList<>();
        List<History> historyEvents = historyRepository.findAll();
        for(History historyEvent : historyEvents) {
            BaseGameModelDTO baseGameModelDTO = BaseGameModelDTO.builder()
                    .name(historyEvent.getName())
                    .comparableValue(Float.parseFloat(historyEvent.getYear()))
                    .comparableValueLabel(historyEvent.getEra())
                    .imageUrl(historyEvent.getImageUrl())
                    .tier(historyEvent.getTier())
                    .build();
            baseGameModelDTOS.add(baseGameModelDTO);
        }
        return baseGameModelDTOS;
    }
}
