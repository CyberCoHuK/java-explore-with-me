package ru.practicum.ewm_service.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewUserDto {
    @NotBlank(message = "Почта не должна быть пустой")
    @Email(message = "Некорректная почта")
    @Size(max = 254, min = 6)
    private String email;
    @Size(max = 250, min = 2)
    @NotBlank(message = "Имя не должен быть пустым")
    private String name;
}
