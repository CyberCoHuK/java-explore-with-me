package ru.practicum.ewm_service.rating.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm_service.events.dto.EventDtoShort;
import ru.practicum.ewm_service.user.dto.UserDtoShort;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RateDto {
    UserDtoShort user;
    EventDtoShort event;
    Boolean mark;
}
