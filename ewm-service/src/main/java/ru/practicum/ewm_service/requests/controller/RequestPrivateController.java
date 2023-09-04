package ru.practicum.ewm_service.requests.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_service.requests.dto.ParticipationRequestDto;
import ru.practicum.ewm_service.requests.service.RequestPrivateService;

import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
public class RequestPrivateController {
    private final RequestPrivateService requestPrivateService;

    @GetMapping
    public List<ParticipationRequestDto> getRequestsOfUser(@PathVariable Long userId) {
        return requestPrivateService.getRequestsOfUser(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        return requestPrivateService.createRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable Long userId, @RequestParam Long requestId) {
        return requestPrivateService.cancelRequest(userId, requestId);
    }
}
