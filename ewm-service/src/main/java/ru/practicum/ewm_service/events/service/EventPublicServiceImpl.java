package ru.practicum.ewm_service.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm_service.events.dto.EventDto;
import ru.practicum.ewm_service.events.mapper.EventMapper;
import ru.practicum.ewm_service.events.model.Event;
import ru.practicum.ewm_service.events.repository.EventRepository;
import ru.practicum.ewm_service.exceptions.exception.BadRequestException;
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
            throw new BadRequestException("Недопустимый временной промежуток.");
        }
        PageRequest page = PageRequest.of(from / size, size);
        List<EventDto> answer;
        if (rangeEnd == null && rangeStart == null) {
            answer = eventRepository.findAllByPublicNoDate(text, categories, paid, LocalDateTime.now(), page).stream()
                    .map(eventMapper::toEventDto).collect(Collectors.toList());
        } else {
            answer = eventRepository.findAllByPublic(text, categories, paid, rangeStart, rangeEnd, page).stream()
                    .map(eventMapper::toEventDto).collect(Collectors.toList());
        }
        if (sorts != null) {
            switch (sorts) {
                case EVENT_DATE:
                    answer.sort(Comparator.comparing(EventDto::getEventDate));
                    break;
                case VIEWS:
                    answer.sort(Comparator.comparing(EventDto::getViews));
                    break;
            }
        }
        if (onlyAvailable) {
            return answer.stream()
                    .filter(e -> e.getParticipantLimit() > e.getConfirmedRequests() || e.getParticipantLimit() == 0)
                    .collect(Collectors.toList());
        }
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
        return eventMapper.toEventDto(event);
    }
}
