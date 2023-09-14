package ru.practicum.ewm_service.rating.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm_service.events.dto.EventDtoShort;
import ru.practicum.ewm_service.rating.service.PublicRatingService;
import ru.practicum.ewm_service.user.dto.UserDtoRate;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@Validated
@RestController
@RequestMapping(path = "/rating")
@RequiredArgsConstructor
public class PublicRatingController {
    private final PublicRatingService publicRatingService;

    @GetMapping("/users")
    public Collection<UserDtoRate> getUsersRating(
            @RequestParam(defaultValue = "true") Boolean sort,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size) {
        return publicRatingService.getUsersRating(sort, size, from);
    }

    @GetMapping("/events")
    public Collection<EventDtoShort> getEventsRating(
            @RequestParam(defaultValue = "true") Boolean sort,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size) {
        return publicRatingService.getEventsRating(sort, size, from);
    }
}
