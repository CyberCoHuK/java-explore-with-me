package ru.practicum.ewm_service.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm_service.events.dto.EventDto;
import ru.practicum.ewm_service.events.mapper.EventMapper;
import ru.practicum.ewm_service.events.model.Event;
import ru.practicum.ewm_service.events.repository.EventRepository;
import ru.practicum.ewm_service.exceptions.exception.ObjectNotFoundException;
import ru.practicum.ewm_service.statclient.Client;
import ru.practicum.ewm_service.utils.Sorts;
import ru.practicum.ewm_service.utils.State;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm_service.utils.Sorts.EVENT_DATE;

@Service
@RequiredArgsConstructor
public class EventPublicServiceImpl implements EventPublicService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final Client statClient;

    @Override
    @Transactional(readOnly = true)
    public Collection<EventDto> getEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd, Boolean onlyAvailable, Sorts sorts, int from,
                                          int size, HttpServletRequest request) {
        if (rangeEnd != null && rangeStart != null && rangeStart.isAfter(rangeEnd)) {
            throw new IllegalArgumentException("Недопустимый временной промежуток.");
        }
        PageRequest page = PageRequest.of(from / size, size);
        Collection<Event> events;
        if (rangeEnd == null && rangeStart == null) {
            events = eventRepository.findAllByPublicNoDate(text, categories, paid, LocalDateTime.now(), onlyAvailable, page);
        } else {
            events = eventRepository.findAllByPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, page);
        }
        statClient.createStat(request);
        if (sorts.equals(EVENT_DATE)) {
            return events.stream()
                    .map(eventMapper::toEventDto)
                    .sorted(Comparator.comparing(EventDto::getEventDate))
                    .collect(Collectors.toList());
        } else {
            return events.stream()
                    .map(eventMapper::toEventDto)
                    .sorted(Comparator.comparing(EventDto::getViews))
                    .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public EventDto getById(Long id, HttpServletRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("События с id = " + id + " не существует"));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ObjectNotFoundException("Событие должно быть опубликовано");
        }
        statClient.createStat(request);
        return eventMapper.toEventDto(event);
    }
}
