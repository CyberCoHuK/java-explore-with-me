package ru.practicum.ewm_service.events.dto;

import ru.practicum.ewm_service.requests.dto.ParticipationRequestDto;

import java.util.Collection;

public class EventRequestStatusUpdateResult {
    private Collection<ParticipationRequestDto> confirmedRequests;
    private Collection<ParticipationRequestDto> rejectedRequests;
}
