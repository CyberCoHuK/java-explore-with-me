package ru.practicum.ewm_service.categories.service;

import ru.practicum.ewm_service.categories.dto.CategoryDto;

import java.util.Collection;

public interface CategoryPublicService {
    Collection<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategoryById(Long catId);
}
