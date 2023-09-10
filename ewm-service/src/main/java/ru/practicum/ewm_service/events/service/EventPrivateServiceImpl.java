package ru.practicum.ewm_service.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm_service.categories.model.Category;
import ru.practicum.ewm_service.categories.repository.CategoryRepository;
import ru.practicum.ewm_service.events.dto.*;
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
import ru.practicum.ewm_service.requests.dto.ParticipationRequestDto;
import ru.practicum.ewm_service.requests.mapper.RequestMapper;
import ru.practicum.ewm_service.requests.model.ParticipationRequest;
import ru.practicum.ewm_service.requests.repository.RequestRepository;
import ru.practicum.ewm_service.statclient.Client;
import ru.practicum.ewm_service.user.model.User;
import ru.practicum.ewm_service.user.repository.UserRepository;
import ru.practicum.ewm_service.utils.Status;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static ru.practicum.ewm_service.utils.State.*;
import static ru.practicum.ewm_service.utils.Status.CONFIRMED;
import static ru.practicum.ewm_service.utils.Status.REJECTED;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventPrivateServiceImpl implements EventPrivateService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final LocationRepository locationRepository;
    private final RateRepository rateRepository;
    private final Client statClient;

    @Override
    public Collection<EventDtoShort> getAllEventsByUser(Long userId, int from, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователя с id = " + userId + " не существует"));
        PageRequest page = PageRequest.of(from / size, size);
        List<Event> answer = eventRepository.findAllByInitiator(user, page);
        List<Long> eventsId = answer.stream().map(Event::getId).collect(Collectors.toList());
        Map<Long, Long> requests = new HashMap<>();
        Map<Long, Long> views = new HashMap<>();
        requestRepository.findConfirmedRequests(eventsId)
                .forEach(stat -> requests.put(stat.getEventId(), stat.getConfirmedRequests()));
        statClient.getViews(eventsId)
                .forEach(view -> views.put(Long.parseLong(view.getEventUri().split("/", 0)[2]), view.getView()));
        return answer.stream().map(event -> EventMapper.toEventDtoShort(event,
                        requests.getOrDefault(event.getId(), 0L),
                        views.getOrDefault(event.getId(), 0L)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventDto createNewEvent(Long userId, NewEventDto newEventDto) {
        if (newEventDto.getEventDate() != null && newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Недопустимый временной промежуток.");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователя с id = " + userId + " не существует"));
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new ObjectNotFoundException("Пользователя с id = " + userId + " не существует"));
        Location location = locationRepository.findByLatAndLon(newEventDto.getLocation().getLat(),
                        newEventDto.getLocation().getLat())
                .orElseGet(() -> locationRepository.save(LocationMapper.toLocation(newEventDto.getLocation())));
        Event event = eventRepository.save(EventMapper.toEvent(newEventDto, category, location, user, LocalDateTime.now(), PENDING));
        return EventMapper.toEventDto(event, requestRepository.findConfirmedRequest(event.getId()),
                statClient.getView(event.getId()), rateRepository.countByEventIdAndRateEquals(event.getId(), TRUE),
                rateRepository.countByEventIdAndRateEquals(event.getId(), FALSE)
        );
    }

    @Override
    public EventDto getEventsByUser(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("События с id = " + eventId + " не существует"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователя с id = " + userId + " не существует"));
        if (!event.getInitiator().equals(user)) {
            throw new IllegalArgumentException("Событие не принадлежит данному пользователю");
        }
        return EventMapper.toEventDto(event, requestRepository.findConfirmedRequest(event.getId()),
                statClient.getView(event.getId()), rateRepository.countByEventIdAndRateEquals(event.getId(), TRUE),
                rateRepository.countByEventIdAndRateEquals(event.getId(), FALSE)
        );
    }

    @Override
    @Transactional
    public EventDto editEventsByUser(Long userId, Long eventId, UpdateEventUserRequest updateEvent) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("События с id = " + eventId + " не существует"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователя с id = " + userId + " не существует"));
        if (!event.getInitiator().equals(user)) {
            throw new ConflictException("Событие не принадлежит данному пользователю");
        }
        if (event.getState().equals(PUBLISHED)) {
            throw new ConflictException("Событие можно изменить только в состоянии ожидания или отмененные");
        }
        if (updateEvent.getCategory() != null) {
            event.setCategory(categoryRepository.findById(updateEvent.getCategory()).orElseThrow(() ->
                    new ObjectNotFoundException("Категории с id = " + updateEvent.getCategory() + " не существует")));
        }
        if (updateEvent.getEventDate() != null && updateEvent.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Недопустимый временной промежуток.");
        }
        if (updateEvent.getStateAction() != null) {
            switch (updateEvent.getStateAction()) {
                case CANCEL_REVIEW:
                    event.setState(CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    event.setState(PENDING);
                    break;
                default:
                    throw new IllegalArgumentException("Недопустимое значение StateAction");
            }
        }
        if (updateEvent.getLocation() != null) {
            Location location = locationRepository.findByLatAndLon(updateEvent.getLocation().getLat(),
                            updateEvent.getLocation().getLat())
                    .orElseGet(() -> locationRepository.save(LocationMapper.toLocation(updateEvent.getLocation())));
            event.setLocation(location);
        }
        Optional.ofNullable(updateEvent.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(updateEvent.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(updateEvent.getEventDate()).ifPresent(event::setEventDate);
        Optional.ofNullable(updateEvent.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(updateEvent.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(updateEvent.getRequestModeration()).ifPresent(event::setRequestModeration);
        Optional.ofNullable(updateEvent.getTitle()).ifPresent(event::setTitle);
        return EventMapper.toEventDto(event, requestRepository.findConfirmedRequest(event.getId()),
                statClient.getView(event.getId()), rateRepository.countByEventIdAndRateEquals(event.getId(), TRUE),
                rateRepository.countByEventIdAndRateEquals(event.getId(), FALSE)
        );
    }

    @Override
    public Collection<ParticipationRequestDto> getRequestsByUser(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("События с id = " + eventId + " не существует"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователя с id = " + userId + " не существует"));
        if (!event.getInitiator().equals(user)) {
            throw new IllegalArgumentException("Событие не принадлежит данному пользователю");
        }
        return requestRepository.findAllByEvent(event).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult editStatusOfRequests(Long userId, Long eventId,
                                                               EventRequestStatusUpdateRequest eventRequestUpdate) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("События с id = " + eventId + " не существует"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователя с id = " + userId + " не существует"));
        if (!event.getInitiator().equals(user)) {
            throw new IllegalArgumentException("Событие не принадлежит данному пользователю");
        }
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            throw new IllegalArgumentException("Для события подтвреждение заявок не требуется");
        }
        if (event.getParticipantLimit().equals(requestRepository.findConfirmedRequest(eventId))) {
            throw new ConflictException("Достигнут лимит заявок для события");
        }
        List<ParticipationRequest> requests = requestRepository.findAllByIdIn(eventRequestUpdate.getRequestIds());
        if (!requests.stream().map(ParticipationRequest::getStatus).allMatch(Status.PENDING::equals)) {
            throw new ConflictException("Изменять можно только запросы находящиеся в ожидании");
        }
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();

        if (eventRequestUpdate.getStatus().equals(REJECTED)) {
            requests.forEach(request -> request.setStatus(REJECTED));
            result.setRejectedRequests(requestRepository.saveAll(requests).stream()
                    .map(RequestMapper::toRequestDto).collect(Collectors.toList()));
        }
        if (eventRequestUpdate.getStatus().equals(CONFIRMED)) {
            for (ParticipationRequest r : requests) {
                if (requestRepository.findConfirmedRequest(eventId).equals(event.getParticipantLimit())) {
                    r.setStatus(REJECTED);
                    result.getRejectedRequests().add(RequestMapper.toRequestDto(requestRepository.save(r)));
                } else {
                    r.setStatus(CONFIRMED);
                    result.getConfirmedRequests().add(RequestMapper.toRequestDto(requestRepository.save(r)));
                }
            }
        }
        eventRepository.save(event);
        return result;
    }
}
