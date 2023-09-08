package ru.practicum.ewm_service.rating.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm_service.events.model.Event;
import ru.practicum.ewm_service.events.repository.EventRepository;
import ru.practicum.ewm_service.exceptions.exception.ConflictException;
import ru.practicum.ewm_service.exceptions.exception.ObjectNotFoundException;
import ru.practicum.ewm_service.rating.dto.RateDto;
import ru.practicum.ewm_service.rating.mapper.RateMapper;
import ru.practicum.ewm_service.rating.model.Rate;
import ru.practicum.ewm_service.rating.repository.RateRepository;
import ru.practicum.ewm_service.requests.repository.RequestRepository;
import ru.practicum.ewm_service.user.model.User;
import ru.practicum.ewm_service.user.repository.UserRepository;

import static ru.practicum.ewm_service.utils.State.PUBLISHED;
import static ru.practicum.ewm_service.utils.Status.CONFIRMED;

@Service
@RequiredArgsConstructor
@Transactional
public class PrivateRatingServiceImpl implements PrivateRatingService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RateRepository rateRepository;
    private final RateMapper rateMapper;
    private final RequestRepository requestRepository;

    @Override
    public RateDto addMark(Long userId, Long eventId, Boolean rate) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("События с id = " + eventId + " не существует"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователя с id = " + userId + " не существует"));
        if (rateRepository.existsByUserAndEvent(user, event)) {
            throw new ConflictException("Поставить оценку повторно нельзя");
        }
        if (event.getInitiator().equals(user)) {
            throw new ConflictException("Нельзя поставить оценку своему событию");
        }
        if (!event.getState().equals(PUBLISHED)) {
            throw new ConflictException("Событие должно быть опубликовано");
        }
        if (!requestRepository.existsByEventAndRequesterAndStatus(event, user, CONFIRMED)) {
            throw new ConflictException("Пользователь должен посетить событие для постановки оценки");
        }
        Rate newRate = Rate.builder()
                .event(event)
                .user(user)
                .rate(rate)
                .build();
        return rateMapper.toRateDto(rateRepository.save(newRate));
    }

    @Override
    public RateDto changeMark(Long userId, Long eventId, Long rateId, Boolean rate) {
        Rate rateUpdate = rateRepository.findByUserIdAndEventIdAndId(userId, eventId, rateId)
                .orElseThrow(() -> new ObjectNotFoundException("Оценки с id = " + rateId + " не существует"));
        rateUpdate.setRate(rate);
        return rateMapper.toRateDto(rateRepository.save(rateUpdate));
    }

    @Override
    public void deleteRate(Long userId, Long eventId, Long rateId) {
        rateRepository.findByUserIdAndEventIdAndId(userId, eventId, rateId)
                .orElseThrow(() -> new ObjectNotFoundException("Оценки с id = " + rateId + " не существует"));
        rateRepository.deleteById(eventId);
    }
}
