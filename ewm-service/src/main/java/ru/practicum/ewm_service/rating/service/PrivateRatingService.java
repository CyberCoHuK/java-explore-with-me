package ru.practicum.ewm_service.rating.service;

import ru.practicum.ewm_service.rating.dto.RateDto;

public interface PrivateRatingService {
    RateDto addMark(Long userId, Long eventId, Boolean rate);

    RateDto changeMark(Long userId, Long eventId, Long rateId, Boolean rate);
}
