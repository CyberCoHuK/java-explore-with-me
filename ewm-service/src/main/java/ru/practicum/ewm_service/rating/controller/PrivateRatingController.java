package ru.practicum.ewm_service.rating.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_service.rating.dto.RateDto;
import ru.practicum.ewm_service.rating.service.PrivateRatingService;

@Validated
@RestController
@RequestMapping(path = "/users/{userId}/events/{eventId}/rate")
@RequiredArgsConstructor
public class PrivateRatingController {
    private final PrivateRatingService privateRatingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RateDto addRate(@PathVariable Long userId, @PathVariable Long eventId,
                           @RequestParam(defaultValue = "true") Boolean rate) {
        return privateRatingService.addMark(userId, eventId, rate);
    }

    @PatchMapping("/{rateId}")
    public RateDto changeRate(@PathVariable Long userId, @PathVariable Long eventId, @PathVariable Long rateId,
                              @RequestParam(defaultValue = "true") Boolean rate) {
        return privateRatingService.changeMark(userId, eventId, rateId, rate);
    }
}
