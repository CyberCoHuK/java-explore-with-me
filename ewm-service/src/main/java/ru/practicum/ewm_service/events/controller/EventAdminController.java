package ru.practicum.ewm_service.events.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_service.events.dto.EventDto;
import ru.practicum.ewm_service.events.dto.UpdateEventAdminRequest;
import ru.practicum.ewm_service.events.service.EventAdminService;
import ru.practicum.ewm_service.utils.State;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static ru.practicum.ewm_service.utils.CommonUtils.DATE_FORMAT;

@Validated
@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
public class EventAdminController {
    private final EventAdminService eventService;

    @GetMapping
    public Collection<EventDto> searchEvents(@RequestParam(required = false) List<Long> users,
                                             @RequestParam(required = false) List<State> states,
                                             @RequestParam(required = false) List<Long> categories,
                                             @RequestParam(required = false)
                                             @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime rangeStart,
                                             @RequestParam(required = false)
                                             @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime rangeEnd,
                                             @RequestParam(defaultValue = "0")
                                             @PositiveOrZero int from,
                                             @RequestParam(defaultValue = "10") @Positive int size) {
        return eventService.searchEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventDto editEventByAdmin(@PathVariable Long eventId,
                                     @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        return eventService.editEventByAdmin(eventId, updateEventAdminRequest);
    }
}
