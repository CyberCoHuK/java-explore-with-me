package ru.practicum.ewm_service.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import static ru.practicum.ewm_service.utils.Sorts.VIEWS;

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
        Comparator<EventDto> comparator;

        if (rangeEnd != null && rangeStart != null && rangeStart.isAfter(rangeEnd)) {
            throw new IllegalArgumentException("Недопустимый временной промежуток.");
        }
        PageRequest page = PageRequest.of(from / size, size);
        Collection<Event> events;
        if (rangeEnd == null && rangeStart == null) {
            events = eventRepository.findAllByAnnotationOrDescriptionContainingIgnoreCaseAndCategoryIdInAndPaidEqualsAndEventDateAfterAndStateEquals(text, text, categories, paid, LocalDateTime.now(), State.PUBLISHED, page);
        } else {
            events = eventRepository.findAllByAnnotationOrDescriptionContainingIgnoreCaseAndCategoryIdInAndPaidEqualsAndEventDateAfterAndEventDateBeforeAndStateEquals(text, text, categories, paid, rangeStart, rangeEnd, State.PUBLISHED, page);
        }
        Collection<EventDto> answer;
        if (onlyAvailable) {
            answer = events.stream()
                    .filter(e -> e.getParticipantLimit() > e.getConfirmedRequests() || e.getParticipantLimit() == 0)
                    .map(eventMapper::toEventDto)
                    .collect(Collectors.toList());
        } else {
            answer = events.stream().map(eventMapper::toEventDto).collect(Collectors.toList());
        }
        statClient.createStat(request);
        if (sorts.equals(EVENT_DATE)) {
            comparator = Comparator.comparing(EventDto::getEventDate);
        } else {
            comparator = Comparator.comparing(EventDto::getViews);
        }
        return answer.stream().sorted(comparator).collect(Collectors.toList());
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
