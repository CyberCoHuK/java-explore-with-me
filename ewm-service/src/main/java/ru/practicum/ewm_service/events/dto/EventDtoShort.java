package ru.practicum.ewm_service.events.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm_service.categories.dto.CategoryDto;
import ru.practicum.ewm_service.user.dto.UserDtoShort;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDtoShort {
    String annotation;
    CategoryDto category;
    Long confirmedRequests;
    LocalDateTime eventDate;
    Long id;
    UserDtoShort initiator;
    Boolean paid;
    String title;
    Long views;
}
