package ru.practicum.ewm_service.rating.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm_service.rating.dto.RateDto;
import ru.practicum.ewm_service.rating.model.Rate;

@RequiredArgsConstructor
@Component
public class RateMapper {
    public RateDto toRateDto(Rate rate) {
        return RateDto.builder()
                .id(rate.getId())
                .event(rate.getEvent().getId())
                .user(rate.getUser().getId())
                .rate(rate.getRate())
                .build();
    }
}