package ru.practicum.ewm_service.events.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_service.events.dto.*;
import ru.practicum.ewm_service.events.service.EventPrivateService;
import ru.practicum.ewm_service.requests.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@Validated
@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
public class EventPrivateController {
    private final EventPrivateService eventService;

    @GetMapping
    public Collection<EventDtoShort> getAllEventsByUser(@PathVariable Long userId,
                                                        @RequestParam(defaultValue = "0")
                                                        @PositiveOrZero int from,
                                                        @RequestParam(defaultValue = "10")
                                                        @Positive int size) {
        return eventService.getAllEventsByUser(userId, from, size);
    }

    @PostMapping
    public EventDto createNewEvent(@PathVariable Long userId, @Valid @RequestBody NewEventDto newEventDto) {
        return eventService.createNewEvent(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    public EventDto getEventsByUser(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.getEventsByUser(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventDto editEventsByUser(@PathVariable Long userId, @PathVariable Long eventId,
                                     @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        return eventService.editEventsByUser(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/{eventId}/requests")
    public Collection<ParticipationRequestDto> getRequestsByUser(@PathVariable Long userId,
                                                                 @PathVariable Long eventId) {
        return eventService.getRequestsByUser(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult editStatusOfRequests(@PathVariable Long userId, @PathVariable Long eventId,
                                                               @Valid @RequestBody
                                                               EventRequestStatusUpdateRequest eventRequestUpdate) {
        return eventService.editStatusOfRequests(userId, eventId, eventRequestUpdate);
    }
}
