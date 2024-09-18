package com.dziem.popapi.repository;

import com.dziem.popapi.model.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
    @Query(value = "select name from apartment where category = 'Poland'" , nativeQuery = true)
    List<String> getPolishCityNames();
}
