package ru.practicum.ewm_service.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoShort {
    @NotBlank(message = "Почта не должна быть пустой")
    @Email(message = "Некорректная почта")
    private String email;
    @NotBlank(message = "Имя не должен быть пустым")
    private String name;
}
