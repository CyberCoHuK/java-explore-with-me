package ru.practicum.ewm_service.requests.service;

import ru.practicum.ewm_service.requests.dto.ParticipationRequestDto;

import java.util.List;

public class RequestPrivateServiceImpl implements RequestPrivateService{
    @Override
    public List<ParticipationRequestDto> getRequestsOfUser(Long userId) {
        return null;
    }

    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        return null;
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        return null;
    }
}
