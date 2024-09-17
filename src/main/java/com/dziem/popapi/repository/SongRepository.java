package com.dziem.popapi.repository;

import com.dziem.popapi.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {
    @Query(value = "SELECT DISTINCT(LAST_UPDATE) FROM SONG", nativeQuery = true)
    List<Date> findAllDates();
    @Query(value = "SELECT * FROM SONG WHERE LAST_UPDATE = ?1", nativeQuery = true)
    List<Song> findAllSongsFromCertainUpdate(LocalDate localDate);
}
