package ru.practicum.ewm_service.rating.mapper;

import ru.practicum.ewm_service.events.mapper.EventMapper;
import ru.practicum.ewm_service.rating.dto.RateDto;
import ru.practicum.ewm_service.rating.model.Rate;
import ru.practicum.ewm_service.user.mapper.UserMapper;

public class RateMapper {
    EventMapper eventMapper;

    public RateDto toRateDto(Rate rate) {
        return RateDto.builder()
                .event(eventMapper.toEventDtoShort(rate.getEvent()))
                .user(UserMapper.toUserDtoShort(rate.getUser()))
                .build();
    }
}