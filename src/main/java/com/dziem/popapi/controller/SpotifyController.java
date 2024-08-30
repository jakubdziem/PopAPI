package com.dziem.popapi.controller;

import com.dziem.popapi.model.ArtistDTO;
import com.dziem.popapi.model.SongDTO;
import com.dziem.popapi.service.ArtistService;
import com.dziem.popapi.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SpotifyController {
    private final ArtistService artistService;
    private final SongService songService;
    @Deprecated
    @GetMapping("/api/v1/spotify")
    public List<ArtistDTO> getTop200Artists() {
        return artistService.getTop200Artists();
    }
    @Deprecated
    @GetMapping("api/v1/spotify/songs")
    public List<SongDTO> getTop200Songs() {
        return songService.getTop200Songs();
    }
    @Deprecated
    @GetMapping("api/v1/spotify/songs/{genre}")
    public List<SongDTO> getTop200SongsGenre(@PathVariable String genre) {
        return songService.getTop200SongsGenre(genre);
    }

}
