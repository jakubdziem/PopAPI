package com.dziem.popapi.controller;

import com.dziem.popapi.model.ArtistDTO;
import com.dziem.popapi.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SpotifyController {
    private final ArtistService artistService;
    @GetMapping("/api/v1/spotify")
    public List<ArtistDTO> getTop200Artists() {
        return artistService.getTop200Artists();
    }


}
