package ru.practicum.ewm_service.categories.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    @NotBlank(message = "Имя не должен быть пустым")
    private String name;
    private Long id;
}
