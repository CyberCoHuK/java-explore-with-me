package ru.practicum.ewm_service.events.service;

import ru.practicum.ewm_service.events.dto.EventDto;
import ru.practicum.ewm_service.utils.Sorts;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EventPublicService {
    Collection<EventDto> getEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, Sorts sorts, int from, int size);

    EventDto getById(Long id);
}
