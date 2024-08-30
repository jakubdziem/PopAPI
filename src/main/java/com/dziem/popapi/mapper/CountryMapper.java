package com.dziem.popapi.mapper;

import com.dziem.popapi.model.Country;
import com.dziem.popapi.model.CountryDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CountryMapper {
    CountryDTO countryToCountryDto(Country country);
}