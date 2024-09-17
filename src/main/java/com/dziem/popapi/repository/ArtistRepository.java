package com.dziem.popapi.repository;

import com.dziem.popapi.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    @Query(value = "SELECT DISTINCT(LAST_UPDATE) FROM ARTIST", nativeQuery = true)
    List<Date> findAllDates();
    @Query(value = "SELECT * FROM ARTIST WHERE LAST_UPDATE = ?1", nativeQuery = true)
    List<Artist> findAllArtistFromCertainUpdate(LocalDate localDate);
}
