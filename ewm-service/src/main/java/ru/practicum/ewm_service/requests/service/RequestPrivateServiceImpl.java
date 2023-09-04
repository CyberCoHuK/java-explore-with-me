package ru.practicum.ewm_service.requests.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm_service.events.model.Event;
import ru.practicum.ewm_service.events.repository.EventRepository;
import ru.practicum.ewm_service.exceptions.exception.ObjectNotFoundException;
import ru.practicum.ewm_service.requests.dto.ParticipationRequestDto;
import ru.practicum.ewm_service.requests.mapper.RequestMapper;
import ru.practicum.ewm_service.requests.model.ParticipationRequest;
import ru.practicum.ewm_service.requests.repository.RequestRepository;
import ru.practicum.ewm_service.user.model.User;
import ru.practicum.ewm_service.user.repository.UserRepository;
import ru.practicum.ewm_service.utils.State;
import ru.practicum.ewm_service.utils.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.ewm_service.utils.Status.CONFIRMED;
import static ru.practicum.ewm_service.utils.Status.PENDING;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestPrivateServiceImpl implements RequestPrivateService {
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequestsOfUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователя с id = " + userId + " не существует"));
        return requestRepository.findAllByRequester(user).stream()
                .map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        log.info("Я В МОМЕНТЕ");
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("События с id = " + eventId + " не существует"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователя с id = " + userId + " не существует"));
        log.info(event.getInitiator().toString());

        if (event.getInitiator().equals(user)) {
            throw new IllegalArgumentException("Нельзя создать запрос на свое событие");
        }
        log.info(event.getState().toString());
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new IllegalArgumentException("Событие должно быть опубликовано");
        }
        log.info("asdf " + requestRepository.existsByRequesterAndEvent(user, event));
        if (requestRepository.existsByRequesterAndEvent(user, event)) {
            throw new IllegalArgumentException("Невозможно отправить повторный запрос");
        }
        Long confirmedRequests = requestRepository.findConfirmedRequests(eventId);
        log.info(confirmedRequests.toString());
        log.info(event.getParticipantLimit().toString());
        if (event.getParticipantLimit() > 0 && Objects.equals(event.getParticipantLimit(), confirmedRequests)) {
            throw new IllegalArgumentException("У события достигнут лимит запросов на участие.");
        }
        ParticipationRequest request = ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .requester(user)
                .event(event)
                .build();
        log.info(event.getRequestModeration().toString());
        if (event.getRequestModeration() && event.getParticipantLimit() > 0) {
            request.setStatus(PENDING);
        } else {
            request.setStatus(CONFIRMED);
        }
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователя с id = " + userId + " не существует"));
        ParticipationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ObjectNotFoundException("Запроса с id = " + requestId + " не существует"));
        if (!request.getRequester().equals(user)) {
            throw new IllegalArgumentException("Запрос не принадлежит пользователю");
        }
        request.setStatus(Status.CANCELED);
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }
}
