package ru.practicum.ewm_service.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm_service.categories.repository.CategoryRepository;
import ru.practicum.ewm_service.events.dto.*;
import ru.practicum.ewm_service.events.mapper.EventMapper;
import ru.practicum.ewm_service.events.model.Event;
import ru.practicum.ewm_service.events.repository.EventRepository;
import ru.practicum.ewm_service.exceptions.exception.ObjectNotFoundException;
import ru.practicum.ewm_service.requests.dto.ParticipationRequestDto;
import ru.practicum.ewm_service.requests.mapper.RequestMapper;
import ru.practicum.ewm_service.requests.model.ParticipationRequest;
import ru.practicum.ewm_service.requests.repository.RequestRepository;
import ru.practicum.ewm_service.user.model.User;
import ru.practicum.ewm_service.user.repository.UserRepository;
import ru.practicum.ewm_service.utils.Status;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.ewm_service.utils.State.*;
import static ru.practicum.ewm_service.utils.Status.CONFIRMED;
import static ru.practicum.ewm_service.utils.Status.REJECTED;

@Service
@RequiredArgsConstructor
public class EventPrivateServiceImpl implements EventPrivateService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final EventMapper eventMapper;

    @Override
    @Transactional(readOnly = true)
    public Collection<EventDtoShort> getAllEventsByUser(Long userId, int from, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователя с id = " + userId + " не существует"));
        PageRequest page = PageRequest.of(from / size, size);
        return eventRepository.findAllByUser(user, page).stream().map(eventMapper::toEventDtoShort).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventDto createNewEvent(Long userId, NewEventDto newEventDto) {
        Event event = eventMapper.toEvent(newEventDto);
        if (event.getEventDate().isAfter(LocalDateTime.now().plusHours(2))) {
            throw new IllegalArgumentException("Недопустимый временной промежуток.");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователя с id = " + userId + " не существует"));
        event.setInitiator(user);
        return eventMapper.toEventDto(eventRepository.save(event));
    }

    @Override
    @Transactional(readOnly = true)
    public EventDto getEventsByUser(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("События с id = " + eventId + " не существует"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователя с id = " + userId + " не существует"));
        if (!event.getInitiator().equals(user)) {
            throw new IllegalArgumentException("Событие не принадлежит данному пользователю");
        }
        return eventMapper.toEventDto(event);
    }

    @Override
    @Transactional
    public EventDto editEventsByUser(Long userId, Long eventId, UpdateEventUserRequest updateEvent) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("События с id = " + eventId + " не существует"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователя с id = " + userId + " не существует"));
        if (!event.getInitiator().equals(user)) {
            throw new IllegalArgumentException("Событие не принадлежит данному пользователю");
        }
        if (updateEvent.getStateAction() != null && event.getState().equals(PUBLISHED)) {
            throw new IllegalArgumentException("Событие можно изменить только в состоянии ожидания или отмененные");
        }
        if (updateEvent.getStateAction() != null) {
            switch (updateEvent.getStateAction()) {
                case CANCEL_REVIEW:
                    if (!event.getState().equals(PENDING)) {
                        event.setState(CANCELED);
                        break;
                    } else {
                        throw new IllegalArgumentException("Можно отклонить событие только если оно в ожидании");
                    }
                case SEND_TO_REVIEW:
                    if (event.getState().equals(CANCELED)) {
                        event.setState(PENDING);
                        break;
                    } else {
                        throw new IllegalArgumentException("Можно выслать на ревью событие только в состоянии отмены");
                    }
                default:
                    throw new IllegalArgumentException("Недопустимое значение StateAction");
            }
        }
        if (updateEvent.getEventDate() != null && updateEvent.getEventDate().isAfter(LocalDateTime.now().plusHours(2))) {
            throw new IllegalArgumentException("Недопустимый временной промежуток.");
        } else {
            event.setEventDate(updateEvent.getEventDate());
        }
        if (updateEvent.getCategory() != null) {
            event.setCategory(categoryRepository.findById(updateEvent.getCategory()).orElseThrow(() ->
                    new ObjectNotFoundException("Категории с id = " + updateEvent.getCategory() + " не существует")));
        }
        Optional.ofNullable(updateEvent.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(updateEvent.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(updateEvent.getLocation()).ifPresent(event::setLocation);
        Optional.ofNullable(updateEvent.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(updateEvent.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(updateEvent.getRequestModeration()).ifPresent(event::setRequestModeration);
        Optional.ofNullable(updateEvent.getTitle()).ifPresent(event::setTitle);
        return eventMapper.toEventDto(eventRepository.save(event));
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ParticipationRequestDto> getRequestsByUser(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("События с id = " + eventId + " не существует"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователя с id = " + userId + " не существует"));
        if (!event.getInitiator().equals(user)) {
            throw new IllegalArgumentException("Событие не принадлежит данному пользователю");
        }
        return requestRepository.findAllByRequesterAndEvent(user, event).stream()
                .map(RequestMapper::toRequestDto).collect(Collectors.toList());
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
        if (event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            throw new IllegalArgumentException("Достигнут лимит заявок для события");
        }
        List<ParticipationRequest> requests = requestRepository.findAllByIdIn(eventRequestUpdate.getRequestIds());
        if (!requests.stream().map(ParticipationRequest::getStatus).allMatch(Status.PENDING::equals)) {
            throw new IllegalArgumentException("Изменять можно только запросы находящиеся в ожидании");
        }
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        if (eventRequestUpdate.getStatus().equals(REJECTED)) {
            requests.forEach(request -> request.setStatus(REJECTED));
            result.setRejectedRequests(requestRepository.saveAll(requests).stream()
                    .map(RequestMapper::toRequestDto).collect(Collectors.toList()));
        }
        if (eventRequestUpdate.getStatus().equals(CONFIRMED)) {
            for (ParticipationRequest r : requests) {
                if (event.getConfirmedRequests().equals(event.getParticipantLimit())) {
                    r.setStatus(REJECTED);
                    result.getRejectedRequests().add(RequestMapper.toRequestDto(requestRepository.save(r)));
                } else {
                    r.setStatus(CONFIRMED);
                    result.getConfirmedRequests().add(RequestMapper.toRequestDto(requestRepository.save(r)));
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                }
            }
        }
        eventRepository.save(event);
        return result;
    }
}
