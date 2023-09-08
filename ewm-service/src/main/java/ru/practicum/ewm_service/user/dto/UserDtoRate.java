package ru.practicum.ewm_service.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoRate {
    private Long id;
    private String name;
    private Long like;
    private Long dislike;
}
