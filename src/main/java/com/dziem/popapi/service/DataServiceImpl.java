package com.dziem.popapi.service;

import com.dziem.popapi.dto.CountryDTO;
import com.dziem.popapi.formatter.SpotifyTopArtistDataFormatter;
import com.dziem.popapi.model.*;
import com.dziem.popapi.repository.*;
import com.dziem.popapi.scrapping.ArtistScrapper;
import com.dziem.popapi.scrapping.SocialMediaScrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DataServiceImpl implements DataService {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final CountryRepository countryRepository;
    private final YearAndPopulationRepository yearAndPopulationRepository;
    private final ArtistRepository artistRepository;
    private final SongRepository songRepository;
    private final CountryService countryService;
    private final DriverRepository driverRepository;
    private final ApartmentRepository apartmentRepository;
    private final SocialMediaRepository socialMediaRepository;
    private final CinemaRepository cinemaRepository;

    @Override
    @Transactional
    public void getData2024_2100() {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("src/main/resources/data/2024-2100 data.txt"))) {
            HashMap<String, Integer> countryNameAndTier = new HashMap<>();
            for (CountryDTO countryDTO : countryService.findCountriesByDistinctYear("2023", false)) {
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

        if (country.getCountryName().equals("Korea")) {
            country.setCountryName(values[1].substring(1, 6) + " Korea");
        }
        country.setGENC(values[values.length - 5].replace("\"", ""));
        country.setFlagUrl("/images/" + country.getGENC().toLowerCase() + ".png");
        yearAndPopulationDefaultSet(country, values, yearAndPopulation);
        return country;
    }

    private void yearAndPopulationDefaultSet(Country country, String[] values, YearAndPopulation yearAndPopulation) {
        yearAndPopulation.setPopulation(values[values.length - 3].replace("\"", ""));
        yearAndPopulation.setAnnualGrowth(values[values.length - 2].replace("\"", "")
                .concat("," + values[values.length - 1].replace("\"", "")));
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
                        -Double.parseDouble(y.getAnnualGrowth().substring(1).replace(',', '.')) :
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
        String path = "src/main/resources/data/songs.txt";
        String dateAndAllSongs = SpotifyTopArtistDataFormatter.formatSpotifyFile(path);
        String[] line = dateAndAllSongs.split("\n");
        for (int i = 0; i < line.length; i++) {
            String[] split = line[i].split(";");
            if (i == 0) {
                date = LocalDate.parse(line[i], FORMATTER);
            } else if (split[4].equals(genre)) {
                Song song = getSong(split, date, genre);
                songRepository.save(song);
            }
        }
    }

    private Song getSong(String[] split, LocalDate lastUpdate, String genre) {
        Song song = new Song();
        song.setLastUpdate(lastUpdate);
        //1 The Weeknd Blinding Lights 4,450,462,626 Pop
        song.setArtistName(split[1]);
        song.setSongName(split[2]);
        song.setTotalStreams(split[3]);
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
        ArtistScrapper.getAndSaveArtistDataToFile();
        String dateAndAllSongs = SpotifyTopArtistDataFormatter.formatSpotifyFile("src/main/resources/data/artist.txt");
        String[] split = dateAndAllSongs.split("\n");
        for (int i = 0; i < split.length; i++) {
            if (i == 0) {
                date = LocalDate.parse(split[i], formatter);
            } else if (!split[i].isEmpty()) {
                Artist artist = getArtist(split[i], date);
                artistRepository.save(artist);
            }
        }
    }

    private Artist getArtist(String line, LocalDate lastUpdate) {
        Artist artist = new Artist();
        artist.setLastUpdate(lastUpdate);
        String[] split = line.split(" ");
        String artistName = "";
        //skip rank
        int i = 1;
        List<String> numbers = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "0");
        while (!split[i].contains(",") || !numbers.contains(split[i].substring(0, 1))) {
            artistName = artistName.concat(split[i]).concat(" ");
            i++;
        }
        artistName = artistName.substring(0, artistName.length() - 1);
        artist.setArtistName(artistName);
        artist.setLeadStreams(split[i]);
        artist.setImageUrl("/images/spotify/" + artist.getArtistName()
                .replace('/', ' ')
                .replace('?', ' ')
                .replace('*', ' ')
                .replace(':', ' ')
                .replace('\"', ' ')
                .replace('\\', ' ')
                .replace('<', ' ')
                .replace('>', ' ')
                .replace('|', ' ') + ".jpg");
        return artist;
    }

    @Override
    public void addSourceToDriver() {
        List<Driver> drivers = driverRepository.findAll().stream().sorted(Comparator.reverseOrder()).toList();
        HashMap<String, String> missedDrivers = new HashMap<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("src/main/resources/imageSource/f1SourceOfPhotos.txt"))) {
            List<String> lines = reader.lines().toList();
            String driverName = "";
            int index = 0;
            int howManyInserts = 0;
            boolean missedDriver = false;
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                Driver driver = drivers.get(index);
                if (i % 3 == 0) {
                    driverName = line.substring(line.indexOf(" ") + 1, line.indexOf("."));
                    if (!driver.getName().equals(driverName)) {
                        missedDriver = true;
                    }
                }
                if (i % 3 == 2) {
                    String imageUrl = line.substring(line.indexOf(":") + 2);
                    if (missedDriver) {
                        missedDrivers.put(driverName, imageUrl);
                        missedDriver = false;
                    } else {
                        driver.setImageSource(imageUrl);
                        System.out.printf("UPDATE DRIVER SET IMAGE_SOURCE = '%s' WHERE NAME = '%s'; \n", imageUrl, driverName);
                        howManyInserts++;
                    }
                    index++;
                }
            }
            System.out.println("MISSED DRIVERS");
            for (String missedDriverName : missedDrivers.keySet()) {
                System.out.println(missedDriverName + " ; " + missedDrivers.get(missedDriverName));
            }
            System.out.println(howManyInserts + " Updates generated");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addSourceApartmentsPoland() {
        List<String> polishCityNames = apartmentRepository.getPolishCityNames();
        for (String cityName : polishCityNames) {
            System.out.printf("UPDATE APARTMENT SET IMAGE_SOURCE = '' WHERE NAME = '%s';\n", cityName);
        }
    }

    @Override
    public void addSourceHistory() {
        try (Stream<String> reader = Files.lines(Paths.get("src/main/resources/imageSource/historySourceOfPhotos.txt"), Charset.forName("Cp1250"))) {
            List<String> lines = reader.toList();
            for(String line : lines) {
                String nameFromSource = line.substring(0, line.indexOf(':'));
                String imageSource = line.substring(line.indexOf(':')+2);
                System.out.printf("UPDATE HISTORY SET IMAGE_SOURCE = '%s' WHERE NAME = '%s';\n", imageSource, nameFromSource);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addSourceCountriesAndApartmentsWorld() {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("src/main/resources/imageSource/countrySourceOfPhotos.txt"))) {
            List<String> lines = reader.lines().toList();
            HashMap<String, String> imageUrls = new HashMap<>();
            for(String line : lines) {
                String[] split = line.split(" ");
                String imageUrl = split[0].substring("C:\\Users\\jakub\\OneDrive\\Pulpit\\imageSourcesGetter".length());
                String imageSource = split[1];
                imageUrls.put(imageUrl, imageSource);
                imageUrl = imageUrl.replace("\\", "/");
                if(imageUrl.substring(8, imageUrl.indexOf(".")).length() > 2) {
                    imageUrl = imageUrl.substring(0,8) + imageUrl.substring(8, 10).toUpperCase() + imageUrl.substring(10);
                }
                System.out.printf("UPDATE COUNTRY SET IMAGE_SOURCE = '%s' WHERE FLAG_URL = '%s';\n", imageSource, imageUrl);
            }
            System.out.println("\nFind\n");
            List<Apartment> apartments =  apartmentRepository.findAllWorld();
            List<Apartment> missingApartments = new ArrayList<>();
            for(Apartment apartment : apartments) {
                if(imageUrls.containsKey(apartment.getImageUrl().toLowerCase().replace("/", "\\"))) {
                    String imageSource = imageUrls.get(apartment.getImageUrl().toLowerCase().replace("/", "\\"));
                    System.out.printf("UPDATE APARTMENT SET IMAGE_SOURCE = '%s' WHERE IMAGE_URL = '%s';\n", imageSource, apartment.getImageUrl());
                } else {
                    missingApartments.add(apartment);
                }
            }
            for(Apartment apartment : missingApartments) {
                System.out.println(apartment);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getDataSocialMedia() {
        SocialMediaScrapper socialMediaScrapper = new SocialMediaScrapper();
        List<String> socialMediaData = socialMediaScrapper.getSocialMediaData();
        for(String line : socialMediaData) {
            String[] split = line.split(";");
            SocialMedia socialMedia = new SocialMedia();
            socialMedia.setName((split[0]));
            socialMedia.setFollowers(split[1]);
            socialMedia.setTier(Integer.valueOf(split[2]));
            socialMedia.setImageUrl(split[3]);
            socialMedia.setType(split[4]);
            socialMediaRepository.save(socialMedia);
        }
    }

    @Override
    public void addSourcesToSocialMedia() {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("src/main/resources/imageSource/socialMediaSourceOfPhotos.txt"))) {
            List<String> lines = reader.lines().toList();
            for(String line : lines) {
                String name = line.substring(0,line.indexOf("-")-1);
                String imageSource = line.substring(line.indexOf(":", line.indexOf(":")+1)+2);
                System.out.printf("UPDATE SOCIAL_MEDIA SET IMAGE_SOURCE = '%s' WHERE NAME = '%s';\n", imageSource, name);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getCelebsData() {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("src/main/resources/data/topCelebsData.txt"))) {
            List<String> lines = reader.lines().toList();
            int ranking = 1;
            for (String line : lines) {
                if (line.startsWith(String.format("%d.", ranking))) {
                    String name = line.substring(line.indexOf(".") + 2);
                    Cinema celeb = Cinema.builder()
                            .name(name)
                            .type("Celebs")
                            .ranking(ranking)
                            .imageUrl(String.format("/images/celebs/%s.png", name.replace(" ", "_")))
                            .imageSource(null)
                            .tier(ranking < 30 ? 1 : 2)
                            .build();
                    ranking++;
                    cinemaRepository.save(celeb);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getTVShowsData() {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("src/main/resources/data/topTVShowsData.txt"))) {
            List<String> lines = reader.lines().toList();
            int ranking = 1;
            for (String line : lines) {
                if (line.startsWith(String.format("%d. ", ranking))) {
                    String name = line.substring(line.indexOf(".") + 2);
                    Cinema show = Cinema.builder()
                            .name(name)
                            .type("TV Shows")
                            .ranking(ranking)
                            .imageUrl(String.format("/images/tv_shows/%s.png", name
                                    .replace(" ", "_")
                                    .replace('/', ' ')
                                    .replace('?', ' ')
                                    .replace('*', ' ')
                                    .replace(':', ' ')
                                    .replace('\"', ' ')
                                    .replace('\\', ' ')
                                    .replace('<', ' ')
                                    .replace('>', ' ')
                                    .replace('|', ' ')))
                            .imageSource(null)
                            .tier(ranking < 30 ? 1 : 2)
                            .build();
                    ranking++;
                    cinemaRepository.save(show);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getMovieData() {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("src/main/resources/data/topMoviesData.txt"))) {
            List<String> lines = reader.lines().toList();
            int ranking = 1;
            for (String line : lines) {
                if (line.startsWith(String.format("%d. ", ranking))) {
                    String name = line.substring(line.indexOf(".") + 2);
                    Cinema movie = Cinema.builder()
                            .name(name)
                            .type("Movies")
                            .ranking(ranking)
                            .imageUrl(String.format("/images/movies/%s.png", name
                                    .replace(" ", "_")
                                    .replace('/', ' ')
                                    .replace('?', ' ')
                                    .replace('*', ' ')
                                    .replace(':', ' ')
                                    .replace('\"', ' ')
                                    .replace('\\', ' ')
                                    .replace('<', ' ')
                                    .replace('>', ' ')
                                    .replace('|', ' ')))
                            .imageSource(null)
                            .tier(ranking < 30 ? 1 : 2)
                            .build();
                    ranking++;
                    cinemaRepository.save(movie);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void addSourcesToCinema(String type, String path) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(path))) {
            List<String> lines = reader.lines().toList();
            for(String line : lines) {
                String name = line.substring(0,line.indexOf("- Image")-1);
                String imageSource = line.substring(line.indexOf("URL:")+5);
                System.out.printf("UPDATE CINEMA SET IMAGE_SOURCE = '%s' WHERE NAME = '%s' AND TYPE = '%s';\n", imageSource, name, type);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void addSourcesToArtist(String path) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(path))) {
            List<String> lines = reader.lines().toList();
            for(String line : lines) {
                String name = line.substring(0,line.indexOf("- Image")-1);
                String imageSource = line.substring(line.indexOf("URL:")+5);
                System.out.printf("UPDATE ARTIST SET IMAGE_SOURCE = '%s' WHERE ARTIST_NAME = '%s';\n", imageSource, name);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void addSourcesToSong(String path) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(path))) {
            List<String> lines = reader.lines().toList();
            for(String line : lines) {
                String[] name = line.substring(0,line.indexOf("- Image")-1).split(";");
                String artistName = name[1];
                String songName = name[0];
                String imageSource = line.substring(line.indexOf("URL:")+5);
                System.out.printf("UPDATE SONG SET IMAGE_SOURCE = '%s' WHERE song_name = '%s' AND artist_name = '%s'; \n", imageSource, songName.replace("'", "''"), artistName.replace("'", "''"));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

