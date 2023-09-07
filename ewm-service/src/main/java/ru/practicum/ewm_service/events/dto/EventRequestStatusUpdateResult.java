package ru.practicum.ewm_service.events.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm_service.requests.dto.ParticipationRequestDto;

import java.util.ArrayList;
import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateResult {
    private Collection<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
    private Collection<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
}
