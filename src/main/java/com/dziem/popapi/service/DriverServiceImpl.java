package com.dziem.popapi.service;

import com.dziem.popapi.model.Driver;
import com.dziem.popapi.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {
    private final DriverRepository driverRepository;
    @Override
    public List<Driver> getTopScoreDrivers() {
        return driverRepository.findAll();
    }
}
