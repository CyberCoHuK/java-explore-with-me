package ru.practicum.ewm_service.events.dto;

import ru.practicum.ewm_service.utils.Status;

import java.util.List;

public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private Status status;
}
