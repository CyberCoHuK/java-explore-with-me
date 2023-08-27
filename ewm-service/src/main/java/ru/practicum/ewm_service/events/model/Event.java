package ru.practicum.ewm_service.events.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm_service.categories.model.Category;
import ru.practicum.ewm_service.user.model.User;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@Entity
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    String annotation;
    Category category;
    Long confirmedRequests;
    LocalDateTime createdOn;
    String description;
    LocalDateTime eventDate;
    Long id;
    User initiator;
    Location location;
    Boolean paid;
    Integer participantLimit;
    LocalDateTime publishedOn;
    Boolean requestModeration;
    String state;
    String title;
    Long views;
}
