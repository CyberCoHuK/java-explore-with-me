package ru.practicum.ewm_service.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm_service.categories.repository.CategoryRepository;
import ru.practicum.ewm_service.events.dto.EventDto;
import ru.practicum.ewm_service.events.dto.UpdateEventAdminRequest;
import ru.practicum.ewm_service.events.mapper.EventMapper;
import ru.practicum.ewm_service.events.model.Event;
import ru.practicum.ewm_service.events.repository.EventRepository;
import ru.practicum.ewm_service.exceptions.exception.ObjectNotFoundException;
import ru.practicum.ewm_service.utils.State;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.ewm_service.utils.State.CANCELED;
import static ru.practicum.ewm_service.utils.State.PUBLISHED;

@Service
@RequiredArgsConstructor
public class EventAdminServiceImpl implements EventAdminService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final EventMapper eventMapper;

    @Override
    @Transactional(readOnly = true)
    public Collection<EventDto> searchEvents(List<Long> users, List<State> states, List<Long> categories,
                                             LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        if (rangeEnd != null && rangeStart != null) {
            if (rangeStart.isAfter(rangeEnd)) {
                throw new IllegalArgumentException("Недопустимый временной промежуток.");
            }
        }
        PageRequest page = PageRequest.of(from / size, size);
        return eventRepository.findAllAdminByData(users, states, categories, rangeStart, rangeEnd, page).stream()
                .map(eventMapper::toEventDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventDto editEventByAdmin(Long eventId, UpdateEventAdminRequest updateEvent) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("События с id = " + eventId + " не существует"));
        if (updateEvent.getEventDate() != null && updateEvent.getEventDate().isAfter(LocalDateTime.now().plusHours(1))) {
            throw new IllegalArgumentException("Недопустимый временной промежуток.");
        } else {
            event.setEventDate(updateEvent.getEventDate());
        }
        if (updateEvent.getStateAction() != null) {
            if (event.getState().equals(PUBLISHED)) {
                throw new IllegalArgumentException("Изменять опубликованные события нельзя");
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
        Optional.ofNullable(updateEvent.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(updateEvent.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(updateEvent.getLocation()).ifPresent(event::setLocation);
        Optional.ofNullable(updateEvent.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(updateEvent.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(updateEvent.getRequestModeration()).ifPresent(event::setRequestModeration);
        Optional.ofNullable(updateEvent.getTitle()).ifPresent(event::setTitle);
        return eventMapper.toEventDto(eventRepository.save(event));
    }
}
