package com.dziem.popapi.service;

import com.dziem.popapi.model.Apartment;
import com.dziem.popapi.repository.ApartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ApartmentServiceImpl implements ApartmentService {
    private final ApartmentRepository apartmentRepository;
    @Override
    public List<Apartment> getApartments(String category) {
        return apartmentRepository.findAll()
                .stream().filter(apartment -> apartment.getCategory().equals(category)).toList();
    }
}
