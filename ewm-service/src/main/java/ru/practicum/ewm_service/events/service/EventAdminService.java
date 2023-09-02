package ru.practicum.ewm_service.events.service;

import ru.practicum.ewm_service.events.dto.EventDto;
import ru.practicum.ewm_service.events.dto.UpdateEventAdminRequest;
import ru.practicum.ewm_service.utils.State;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EventAdminService {
    Collection<EventDto> searchEvents(List<Long> users, List<State> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    EventDto editEventByAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);
}
