package ru.practicum.ewm_service.events.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm_service.events.model.Location;

import java.time.LocalDateTime;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventUserRequest {
    String annotation;
    List<Long> category;
    String description;
    LocalDateTime eventDate;
    List<Location> location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    String stateAction;
    String title;
}
