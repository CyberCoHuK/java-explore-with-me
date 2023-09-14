package ru.practicum.ewm_service.rating.service;

import ru.practicum.ewm_service.events.dto.EventDtoShort;
import ru.practicum.ewm_service.user.dto.UserDtoRate;

import java.util.Collection;

public interface PublicRatingService {
    Collection<EventDtoShort> getEventsRating(Boolean sort, int size, int from);

    Collection<UserDtoRate> getUsersRating(Boolean sort, int size, int from);
}
