package com.dziem.popapi.service;

import com.dziem.popapi.formatter.FirstLineOfFormatSongsFiles;
import com.dziem.popapi.formatter.SpotifyTopArtistDataFormatter;
import com.dziem.popapi.model.*;
import com.dziem.popapi.repository.ArtistRepository;
import com.dziem.popapi.repository.CountryRepository;
import com.dziem.popapi.repository.SongRepository;
import com.dziem.popapi.repository.YearAndPopulationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataServiceImpl implements DataService {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final CountryRepository countryRepository;
    private final YearAndPopulationRepository yearAndPopulationRepository;
    private final ArtistRepository artistRepository;
    private final SongRepository songRepository;
    private final CountryService countryService;
    @Override
    @Transactional
    public void getData2024_2100() {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("src/main/resources/data/2024-2100 data.txt"))) {
            HashMap<String, Integer> countryNameAndTier = new HashMap<>();
            for(CountryDTO countryDTO : countryService.findCountriesByDistinctYear("2023", false)) {
                String countryName = countryDTO.getYearAndPopulations().getFirst().getCountry().getCountryName();
                Integer tier = countryDTO.getYearAndPopulations().getFirst().getTier();
                countryNameAndTier.put(countryName, tier);
            }
            List<Country> countries = new ArrayList<>();
            reader.lines().skip(1).forEach(line -> {
                Country country = getCountry(line, countryNameAndTier);
                countries.add(country);
            });
            countryRepository.saveAll(countries); // Batch save
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Country getCountry(String line, HashMap<String, Integer> countryNameAndTier) {
        String[] values = line.split(",");
        YearAndPopulation yearAndPopulation = new YearAndPopulation();
        yearAndPopulation.setYearOfMeasurement(values[values.length - 4].replace("\"", ""));
        Country country = new Country();
        String countryName = values[0].replace("\"", "");
        country.setCountryName(countryName);

        Integer tier = countryNameAndTier.get(countryName);
        yearAndPopulation.setTier(tier);

        if(country.getCountryName().equals("Korea")) {
            country.setCountryName(values[1].substring(1,6) + " Korea");
        }
        country.setGENC(values[values.length - 5].replace("\"", ""));
        country.setFlagUrl("/images/" + country.getGENC().toLowerCase() + ".png");
        yearAndPopulationDefaultSet(country, values, yearAndPopulation);
        return country;
    }
    private void yearAndPopulationDefaultSet(Country country, String[] values, YearAndPopulation yearAndPopulation) {
        yearAndPopulation.setPopulation(values[values.length-3].replace("\"", ""));
        yearAndPopulation.setAnnualGrowth(values[values.length-2].replace("\"", "")
                .concat(","+values[values.length-1].replace("\"", "")));
        yearAndPopulation.setCountry(country);
        country.getYearAndPopulations().add(yearAndPopulation);
    }

    @Override
    @Transactional
    public void getData2100_2150() {
        List<YearAndPopulation> list2100 = yearAndPopulationRepository.findByYearOfMeasurement("2100");
        List<YearAndPopulation> toSave = new ArrayList<>();
        String currentYear = "2100";

        for (int i = 1; i <= 50; i++) {
            currentYear = Integer.parseInt(currentYear) >= 2109 ? "21" + i : "210" + i;
            for (YearAndPopulation y : list2100) {
                YearAndPopulation newEntry = new YearAndPopulation();
                newEntry.setAnnualGrowth(y.getAnnualGrowth());
                newEntry.setYearOfMeasurement(currentYear);
                newEntry.setCountry(y.getCountry());
                newEntry.setTier(y.getTier());

                double annualGrowth = y.getAnnualGrowth().startsWith("-") ?
                        -Double.parseDouble(y.getAnnualGrowth().substring(1).replace(',','.')) :
                        Double.parseDouble(y.getAnnualGrowth().replace(',', '.'));

                BigDecimal population = new BigDecimal(y.getPopulation());
                BigDecimal populationCalculated = population.add(population.multiply(BigDecimal.valueOf(annualGrowth / 100)));
                newEntry.setTier(y.getTier());
                newEntry.setPopulation(populationCalculated.toString());
                toSave.add(newEntry);
            }
        }
        yearAndPopulationRepository.saveAll(toSave); // Batch save
    }



    @Override
    @Transactional
    public void getDataSpotifyTopSongs(String genre) {
        LocalDate date = LocalDate.MIN;
        String path = "src/main/resources/spotifyTopSongsData" + genre + ".txt";
        FirstLineOfFormatSongsFiles.formatSpotifyFile(path);
        String dateAndAllArtists = SpotifyTopArtistDataFormatter.formatSpotifyFile(path);
        String[] split = dateAndAllArtists.split("\n");
        for(int i = 0; i < split.length; i++) {
            if(i==0) {
                date = LocalDate.parse(split[i], FORMATTER);
            } else {
                Song song = getSong(split[i], date, genre);
                songRepository.save(song);
            }
        }
    }

    private Song getSong(String line, LocalDate lastUpdate, String genre) {
        Song song = new Song();
        song.setLastUpdate(lastUpdate);
        String[] split = line.split(";");
        String[] name = split[0].split(" ");
        String[] streams = split[1].split(" ");
        String songName = "";
        for(int i = 2; i < name.length; i++) {
            songName = songName.concat(name[i]).concat(i == name.length-1 ? "" : " ");
        }
        song.setSongName(songName);
        String artistName = "";
        int i = 0;
        boolean canRun = true;
        while(canRun) {
            try {
                int compared = new BigDecimal(streams[i].replace(",","")).compareTo(new BigDecimal("100000"));
                if(compared > 0) {
                    canRun = false;
                } else {
                    artistName = artistName.concat(streams[i]).concat(" ");
                }
                i++;
            }
            catch(NumberFormatException e) {
                artistName = artistName.concat(streams[i]).concat(" ");
                i++;
            }
        }
        song.setTotalStreams(streams[i-1]);
        song.setArtistName(artistName.substring(0,artistName.length()-1));
        song.setImageUrl("/images/spotify/songs/" + song.getSongName()
                .replace('/', ' ')
                .replace('?', ' ')
                .replace('*', ' ')
                .replace(':', ' ')
                .replace('\"', ' ')
                .replace('\\', ' ')
                .replace('<', ' ')
                .replace('>', ' ')
                .replace('|', ' ')
                + " - " + song.getArtistName().replace('/', ' ').replace("?", "") + ".jpg");
        song.setGenre(genre);
        return song;
    }

    @Override
    public void getDataSpotifyTopArtists() {
        LocalDate date = LocalDate.MIN;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String dateAndAllSongs = SpotifyTopArtistDataFormatter.formatSpotifyFile("src/main/resources/spotifyTopArtistData.txt");
        String[] split = dateAndAllSongs.split("\n");
        for(int i = 0; i < split.length; i++) {
            if(i==0) {
                date = LocalDate.parse(split[i], formatter);
            } else {
                Artist artist = getArtist(split[i], date);
                artistRepository.save(artist);
            }
        }
    }

    private Artist getArtist(String line, LocalDate lastUpdate) {
        Artist artist = new Artist();
        artist.setLastUpdate(lastUpdate);
        String[] split = line.split(";");
        String[] name = split[0].split(" ");
        String[] streams = split[1].split(" ");
        String artistName = "";
        for(int i = 1; i <= name.length/2; i++) {
            artistName = artistName.concat(name[i]).concat(i == name.length/2 ? "" : " ");
        }
        artist.setArtistName(artistName);
        artist.setLeadStreams(streams[0]);
        if(artist.getArtistName().contains("/")) {
            artist.setImageUrl("/images/spotify/" + artistName.replace('/', ' ') + ".jpg");
        } else {
            artist.setImageUrl("/images/spotify/" + artistName + ".jpg");
        }


        return artist;
    }


}
