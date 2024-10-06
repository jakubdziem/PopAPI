package com.dziem.popapi.service;


public interface DataService {
     void getData2024_2100();
     void getData2100_2150();
     void getDataSpotifyTopArtists();
     void getDataSpotifyTopSongs(String genre);
     void addSourceToDriver();
     void addSourceApartmentsPoland();
     void addSourceHistory();

    void addSourceCountriesAndApartmentsWorld();

    void getDataSocialMedia();

    void addSourcesToSocialMedia();
    void getCelebsData();
    void getTVShowsData();
    void getMovieData();

    void addSourcesToCinema(String type, String path);

    void addSourcesToArtist(String path);
}
