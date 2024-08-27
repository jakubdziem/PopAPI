package com.dziem.popapi.repository;

import com.dziem.popapi.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long> {
}
