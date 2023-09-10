package ru.practicum.ewm_service.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm_service.categories.repository.CategoryRepository;
import ru.practicum.ewm_service.events.dto.EventDto;
import ru.practicum.ewm_service.events.dto.UpdateEventAdminRequest;
import ru.practicum.ewm_service.events.mapper.EventMapper;
import ru.practicum.ewm_service.events.mapper.LocationMapper;
import ru.practicum.ewm_service.events.model.Event;
import ru.practicum.ewm_service.events.model.Location;
import ru.practicum.ewm_service.events.repository.EventRepository;
import ru.practicum.ewm_service.events.repository.LocationRepository;
import ru.practicum.ewm_service.exceptions.exception.BadRequestException;
import ru.practicum.ewm_service.exceptions.exception.ConflictException;
import ru.practicum.ewm_service.exceptions.exception.ObjectNotFoundException;
import ru.practicum.ewm_service.rating.repository.RateRepository;
import ru.practicum.ewm_service.requests.repository.RequestRepository;
import ru.practicum.ewm_service.statclient.Client;
import ru.practicum.ewm_service.utils.State;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static ru.practicum.ewm_service.utils.State.*;

@Service
@RequiredArgsConstructor
public class EventAdminServiceImpl implements EventAdminService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;
    private final RateRepository rateRepository;
    private final Client statClient;

    @Override
    @Transactional(readOnly = true)
    public Collection<EventDto> searchEvents(List<Long> users, List<State> states, List<Long> categories,
                                             LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        if (rangeEnd != null && rangeStart != null && rangeStart.isAfter(rangeEnd)) {
            throw new IllegalArgumentException("Недопустимый временной промежуток.");
        }
        PageRequest page = PageRequest.of(from / size, size);
        List<Event> answer = eventRepository.findAllAdminByData(users, states, categories, rangeStart, rangeEnd, page);
        List<Long> eventsId = answer.stream().map(Event::getId).collect(Collectors.toList());
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
        return answer.stream().map(event -> EventMapper.toEventDto(event,
                requests.getOrDefault(event.getId(), 0L),
                views.getOrDefault(event.getId(), 0L),
                likes.getOrDefault(event.getId(), 0L),
                dislikes.getOrDefault(event.getId(), 0L)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventDto editEventByAdmin(Long eventId, UpdateEventAdminRequest updateEvent) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("События с id = " + eventId + " не существует"));
        if (updateEvent.getEventDate() != null && updateEvent.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new BadRequestException("Недопустимый временной промежуток.");
        }
        if (updateEvent.getStateAction() != null) {
            if (!event.getState().equals(PENDING)) {
                throw new ConflictException("Изменять можно только ожидающие события");
            }
            switch (updateEvent.getStateAction()) {
                case REJECT_EVENT:
                    event.setState(CANCELED);
                    break;
                case PUBLISH_EVENT:
                    event.setState(PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                default:
                    throw new IllegalArgumentException("Недопустимое значение State");
            }
        }
        if (updateEvent.getCategory() != null) {
            event.setCategory(categoryRepository.findById(updateEvent.getCategory()).orElseThrow(() ->
                    new ObjectNotFoundException("Категории с id = " + updateEvent.getCategory() + " не существует")));
        }
        if (updateEvent.getLocation() != null) {
            System.out.println(updateEvent.getLocation());
            Location location = locationRepository.findByLatAndLon(updateEvent.getLocation().getLat(),
                            updateEvent.getLocation().getLat())
                    .orElseGet(() -> locationRepository.save(LocationMapper.toLocation(updateEvent.getLocation())));
            event.setLocation(location);
        }
        Optional.ofNullable(updateEvent.getEventDate()).ifPresent(event::setEventDate);
        Optional.ofNullable(updateEvent.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(updateEvent.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(updateEvent.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(updateEvent.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(updateEvent.getRequestModeration()).ifPresent(event::setRequestModeration);
        Optional.ofNullable(updateEvent.getTitle()).ifPresent(event::setTitle);
        eventRepository.save(event);
        return EventMapper.toEventDto(event, requestRepository.findConfirmedRequest(event.getId()),
                statClient.getView(event.getId()), rateRepository.countByEventIdAndRateEquals(event.getId(), TRUE),
                rateRepository.countByEventIdAndRateEquals(event.getId(), FALSE)
        );
    }
}
