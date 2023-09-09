package ru.practicum.ewm_service.events.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm_service.categories.mapper.CategoriesMapper;
import ru.practicum.ewm_service.categories.model.Category;
import ru.practicum.ewm_service.events.dto.EventDto;
import ru.practicum.ewm_service.events.dto.EventDtoShort;
import ru.practicum.ewm_service.events.dto.NewEventDto;
import ru.practicum.ewm_service.events.model.Event;
import ru.practicum.ewm_service.events.model.Location;
import ru.practicum.ewm_service.user.mapper.UserMapper;
import ru.practicum.ewm_service.user.model.User;
import ru.practicum.ewm_service.utils.State;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class EventMapper {
    public static Event toEvent(NewEventDto newEventDto, Category category, Location location, User user, LocalDateTime now,
                                State pending) {
        return Event.builder()
                .category(category)
                .location(location)
                .initiator(user)
                .annotation(newEventDto.getAnnotation())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .title(newEventDto.getTitle())
                .createdOn(now)
                .state(pending)
                .build();
    }

    public static EventDto toEventDto(Event event) {
        return EventDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoriesMapper.toCategoryDto(event.getCategory()))
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.toUserDtoShort(event.getInitiator()))
                .location(LocationMapper.toLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .build();
    }

    public static EventDtoShort toEventDtoShort(Event event) {
        return EventDtoShort.builder()
                .annotation(event.getAnnotation())
                .category(CategoriesMapper.toCategoryDto(event.getCategory()))
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.toUserDtoShort(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();
    }
}
