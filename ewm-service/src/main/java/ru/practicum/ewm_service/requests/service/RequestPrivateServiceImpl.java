package ru.practicum.ewm_service.requests.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm_service.events.model.Event;
import ru.practicum.ewm_service.events.repository.EventRepository;
import ru.practicum.ewm_service.exceptions.exception.ConflictException;
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
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("События с id = " + eventId + " не существует"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователя с id = " + userId + " не существует"));
        if (event.getInitiator().equals(user)) {
            throw new ConflictException("Нельзя создать запрос на свое событие");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Событие должно быть опубликовано");
        }
        if (requestRepository.existsByRequesterAndEvent(user, event)) {
            throw new ConflictException("Невозможно отправить повторный запрос");
        }
        Long confirmedRequests = requestRepository.findConfirmedRequest(eventId);
        if (event.getParticipantLimit() > 0 && Objects.equals(event.getParticipantLimit(), confirmedRequests)) {
            throw new ConflictException("У события достигнут лимит запросов на участие.");
        }
        ParticipationRequest request = ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .requester(user)
                .event(event)
                .build();
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
