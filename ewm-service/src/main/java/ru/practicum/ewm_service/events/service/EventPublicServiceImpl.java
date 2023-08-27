package ru.practicum.ewm_service.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm_service.events.repository.EventRepository;

@Service
@RequiredArgsConstructor
public class EventPublicServiceImpl implements EventAdminService {
    private final EventRepository eventRepository;
}
