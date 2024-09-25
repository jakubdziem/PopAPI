package com.dziem.popapi.repository;

import com.dziem.popapi.model.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CinemaRepository extends JpaRepository<Cinema, Long> {
    @Query(value = "select * from cinema where type = ?1" , nativeQuery = true)
    List<Cinema> findAllByType(String type);

}
