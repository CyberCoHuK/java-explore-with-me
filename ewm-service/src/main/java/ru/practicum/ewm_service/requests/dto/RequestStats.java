package ru.practicum.ewm_service.requests.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestStats {
    Long eventId;
    Long confirmedRequests;
}