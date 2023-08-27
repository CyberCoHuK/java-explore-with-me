package ru.practicum.ewm_service.categories.mapper;

import ru.practicum.ewm_service.categories.dto.CategoryDto;
import ru.practicum.ewm_service.categories.model.Category;

public class CategoriesMapper {
    public static Category toCategory(CategoryDto categoryDto) {
        return Category.builder()
                .name(categoryDto.getName())
                .build();
    }

    public static CategoryDto toCategoryDtoR(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
