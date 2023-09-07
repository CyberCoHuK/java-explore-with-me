package ru.practicum.ewm_service.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm_service.categories.dto.CategoryDto;
import ru.practicum.ewm_service.user.dto.UserDtoShort;
import ru.practicum.ewm_service.utils.State;

import java.time.LocalDateTime;

import static ru.practicum.ewm_service.utils.CommonUtils.DATE_FORMAT;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    String annotation;
    CategoryDto category;
    Long confirmedRequests;
    @JsonFormat(pattern = DATE_FORMAT, shape = JsonFormat.Shape.STRING)
    LocalDateTime createdOn;
    String description;
    @JsonFormat(pattern = DATE_FORMAT, shape = JsonFormat.Shape.STRING)
    LocalDateTime eventDate;
    Long id;
    UserDtoShort initiator;
    LocationDto location;
    Boolean paid;
    Long participantLimit;
    @JsonFormat(pattern = DATE_FORMAT, shape = JsonFormat.Shape.STRING)
    LocalDateTime publishedOn;
    Boolean requestModeration;
    State state;
    String title;
    Long views;
}
