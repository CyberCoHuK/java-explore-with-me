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
import ru.practicum.ewm_service.requests.repository.RequestRepository;
import ru.practicum.ewm_service.statclient.Client;
import ru.practicum.ewm_service.utils.Sorts;
import ru.practicum.ewm_service.utils.State;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventPublicServiceImpl implements EventPublicService {
    private final EventRepository eventRepository;
    private final Client statClient;
    private final RequestRepository requestRepository;

    @Override
    @Transactional(readOnly = true)
    public Collection<EventDto> getEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd, Boolean onlyAvailable, Sorts sort, int from,
                                          int size, HttpServletRequest request) {
        if (rangeEnd != null && rangeStart != null && rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException("Недопустимый временной промежуток.");
        }
        Pageable page = PageRequest.of(from / size, size);
        List<EventDto> eventsDto;
        if (rangeEnd == null && rangeStart == null) {
            eventsDto = eventRepository.findAllByPublicNoDate(text, categories, paid, LocalDateTime.now()).stream()
                    .map(EventMapper::toEventDto).collect(Collectors.toList());
        } else {
            eventsDto = eventRepository.findAllByPublic(text, categories, paid, rangeStart, rangeEnd).stream()
                    .map(EventMapper::toEventDto).collect(Collectors.toList());
        }
        eventsDto.forEach(e ->
                e.setConfirmedRequests(requestRepository.findConfirmedRequests(e.getId())));
        eventsDto.forEach(e -> e.setViews(statClient.getView(e.getId())));
        if (onlyAvailable) {
            eventsDto = eventsDto.stream()
                    .filter(e -> e.getParticipantLimit() > e.getConfirmedRequests() || e.getParticipantLimit() == 0)
                    .collect(Collectors.toList());
        }
        if (sort != null) {
            switch (sort) {
                case EVENT_DATE:
                    eventsDto.sort(Comparator.comparing(EventDto::getEventDate));
                    break;
                case VIEWS:
                    eventsDto.sort(Comparator.comparingLong(EventDto::getViews));
                    break;
                case LIKE:
                    eventsDto.sort(Comparator.comparingLong(EventDto::getLike));
                    break;
                case DISLIKE:
                    eventsDto.sort(Comparator.comparingLong(EventDto::getDislike));
                    break;
            }
        }
        Collections.reverse(eventsDto);
        int start = (int) page.getOffset();
        int end = Math.min((start + page.getPageSize()), eventsDto.size());
        List<EventDto> answer = eventsDto.subList(start, end);
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
        EventDto eventDto = EventMapper.toEventDto(event);
        eventDto.setConfirmedRequests(requestRepository.findConfirmedRequests(eventDto.getId()));
        eventDto.setViews(statClient.getView(eventDto.getId()));
        statClient.createStat(request);
        return eventDto;
    }
}
