package ru.practicum.ewm_service.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm_service.events.dto.EventDto;
import ru.practicum.ewm_service.events.mapper.EventMapper;
import ru.practicum.ewm_service.events.model.Event;
import ru.practicum.ewm_service.events.repository.EventRepository;
import ru.practicum.ewm_service.exceptions.exception.BadRequestException;
import ru.practicum.ewm_service.exceptions.exception.ObjectNotFoundException;
import ru.practicum.ewm_service.rating.repository.RateRepository;
import ru.practicum.ewm_service.requests.repository.RequestRepository;
import ru.practicum.ewm_service.statclient.Client;
import ru.practicum.ewm_service.utils.Sorts;
import ru.practicum.ewm_service.utils.State;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Service
@RequiredArgsConstructor
public class EventPublicServiceImpl implements EventPublicService {
    private final EventRepository eventRepository;
    private final Client statClient;
    private final RequestRepository requestRepository;
    private final RateRepository rateRepository;

    @Override
    @Transactional(readOnly = true)
    public Collection<EventDto> getEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd, Boolean onlyAvailable, Sorts sort, int from,
                                          int size, HttpServletRequest request) {
        if (rangeEnd != null && rangeStart != null && rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException("Недопустимый временной промежуток.");
        }
        Pageable page = PageRequest.of(from / size, size);
        List<Event> events;
        if (rangeEnd == null && rangeStart == null) {
            events = eventRepository.findAllByPublicNoDate(text, categories, paid, LocalDateTime.now());
        } else {
            events = eventRepository.findAllByPublic(text, categories, paid, rangeStart, rangeEnd);
        }
        List<Long> eventsId = events.stream().map(Event::getId).collect(Collectors.toList());
        Map<Long, Long> requests = new HashMap<>();
        Map<Long, Long> views = new HashMap<>();
        Map<Long, Long> likes = new HashMap<>();
        Map<Long, Long> dislikes = new HashMap<>();
        requestRepository.findConfirmedRequests(eventsId)
                .forEach(stat -> requests.put(stat.getEventId(), stat.getConfirmedRequests()));
        statClient.getViews(eventsId)
                .forEach(view -> views.put(Long.parseLong(view.getEventUri().split("/", 0)[2]), view.getView()));
        rateRepository.findRate(eventsId, TRUE).forEach(like -> likes.put(like.getEvent(), like.getRate()));
        rateRepository.findRate(eventsId, TRUE).forEach(dislike -> dislikes.put(dislike.getEvent(), dislike.getRate()));
        List<EventDto> eventDto = events.stream().map(event -> EventMapper.toEventDto(event,
                        requests.getOrDefault(event.getId(), 0L),
                        views.getOrDefault(event.getId(), 0L),
                        likes.getOrDefault(event.getId(), 0L),
                        dislikes.getOrDefault(event.getId(), 0L)))
                .collect(Collectors.toList());
        if (onlyAvailable) {
            eventDto = eventDto.stream()
                    .filter(e -> e.getParticipantLimit() > e.getConfirmedRequests() || e.getParticipantLimit() == 0)
                    .collect(Collectors.toList());
        }
        if (sort != null) {
            switch (sort) {
                case EVENT_DATE:
                    eventDto.sort(Comparator.comparing(EventDto::getEventDate));
                    break;
                case VIEWS:
                    eventDto.sort(Comparator.comparingLong(EventDto::getViews));
                    break;
                case LIKE:
                    eventDto.sort(Comparator.comparingLong(EventDto::getLike));
                    break;
                case DISLIKE:
                    eventDto.sort(Comparator.comparingLong(EventDto::getDislike));
                    break;
            }
        }
        Collections.reverse(eventDto);
        int start = (int) page.getOffset();
        int end = Math.min((start + page.getPageSize()), eventDto.size());
        List<EventDto> answer = eventDto.subList(start, end);
        statClient.createStat(request);
        return answer;
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
        return EventMapper.toEventDto(event, requestRepository.findConfirmedRequest(event.getId()),
                statClient.getView(event.getId()), rateRepository.countByEventIdAndRateEquals(event.getId(), TRUE),
                rateRepository.countByEventIdAndRateEquals(event.getId(), FALSE));
    }
}
