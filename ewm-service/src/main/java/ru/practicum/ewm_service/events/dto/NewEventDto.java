package ru.practicum.ewm_service.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.ewm_service.utils.CommonUtils.DATE_FORMAT;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    @NotBlank
    @Size(max = 2000, min = 20)
    String annotation;
    @NotNull
    Long category;
    @NotBlank
    @Size(max = 7000, min = 20)
    String description;
    @NotNull
    @JsonFormat(pattern = DATE_FORMAT, shape = JsonFormat.Shape.STRING)
    LocalDateTime eventDate;
    @NotNull
    LocationDto location;
    Boolean paid = false;
    @PositiveOrZero
    Long participantLimit = 0L;
    Boolean requestModeration = true;
    @NotBlank
    @Size(max = 120, min = 3)
    String title;
}
