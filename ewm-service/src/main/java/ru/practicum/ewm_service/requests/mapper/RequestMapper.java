package ru.practicum.ewm_service.requests.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm_service.requests.dto.ParticipationRequestDto;
import ru.practicum.ewm_service.requests.model.ParticipationRequest;

@RequiredArgsConstructor
@Component
public class RequestMapper {
    public static ParticipationRequestDto toRequestDto(ParticipationRequest request) {
        return ParticipationRequestDto.builder()
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .id(request.getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }
}
