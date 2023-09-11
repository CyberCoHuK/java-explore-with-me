package ru.practicum.ewm_service.rating.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RateDto {
    Long id;
    Long user;
    Long event;
    Boolean rate;
}
