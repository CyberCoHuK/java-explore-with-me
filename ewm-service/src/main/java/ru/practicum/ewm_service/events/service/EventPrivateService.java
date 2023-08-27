package ru.practicum.ewm_service.events.service;

import ru.practicum.ewm_service.events.dto.*;
import ru.practicum.ewm_service.requests.dto.ParticipationRequestDto;

import java.util.Collection;

public interface EventPrivateService {
    Collection<EventDto> getAllEventsByUser(Long userId, int from, int size);

    EventDto createNewEvent(Long userId, NewEventDto newEventDto);

    EventDto getEventsByUser(Long userId, Long eventId);

    EventDto editEventsByUser(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    Collection<ParticipationRequestDto> getRequestsByUser(Long userId, Long eventId);

    EventRequestStatusUpdateResult editStatusOfRequests(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestUpdate);
}
