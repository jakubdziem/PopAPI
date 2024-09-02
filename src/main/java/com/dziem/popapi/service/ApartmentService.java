package com.dziem.popapi.service;

import com.dziem.popapi.model.Apartment;

import java.util.List;

public interface ApartmentService {

    List<Apartment> getApartments(String country);
}
