package ru.practicum.ewm_service.events.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm_service.categories.dto.CategoryDto;
import ru.practicum.ewm_service.events.model.Location;
import ru.practicum.ewm_service.user.dto.UserDtoShort;

import java.time.LocalDateTime;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    String annotation;
    CategoryDto category;
    Long confirmedRequests;
    LocalDateTime createdOn;
    String description;
    LocalDateTime eventDate;
    Long id;
    UserDtoShort initiator;
    List<Location> location;
    Boolean paid;
    Integer participantLimit;
    LocalDateTime publishedOn;
    Boolean requestModeration;
    String state;
    String title;
    Long views;
}
