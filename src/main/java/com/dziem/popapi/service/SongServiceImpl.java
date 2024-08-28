package com.dziem.popapi.service;

import com.dziem.popapi.mapper.SongMapper;
import com.dziem.popapi.model.Song;
import com.dziem.popapi.model.SongDTO;
import com.dziem.popapi.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {
    private final SongRepository songRepository;
    private final SongMapper songMapper;
    @Override
    public List<SongDTO> getTop200Songs() {
        List<Song> songs = songRepository.findAll();
        List<SongDTO> songDTOS = new ArrayList<>();
        for(Song song : songs) {
            songDTOS.add(songMapper.songToSongDTO(song));
        }
        return songDTOS;
    }

    @Override
    public List<SongDTO> getTop200SongsGenre(String genre) {
        List<Song> songs = songRepository.findAll().stream().filter(song -> song.getGenre().equals(genre)).toList();
        List<SongDTO> songDTOS = new ArrayList<>();
        for(Song song : songs) {
            songDTOS.add(songMapper.songToSongDTO(song));
        }
        return songDTOS;
    }
}
