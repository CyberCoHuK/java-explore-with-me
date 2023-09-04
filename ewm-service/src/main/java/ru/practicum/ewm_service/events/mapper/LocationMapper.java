package ru.practicum.ewm_service.events.mapper;

import ru.practicum.ewm_service.events.dto.LocationDto;
import ru.practicum.ewm_service.events.model.Location;

public class LocationMapper {

    public static Location toLocation(LocationDto locationDto) {
        return Location.builder()
                .lon(locationDto.getLon())
                .lat(locationDto.getLat())
                .build();
    }

    public static LocationDto toLocationDto(Location location) {
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }

}