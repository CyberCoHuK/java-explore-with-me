package ru.practicum.ewm_service.categories.service;

import ru.practicum.ewm_service.categories.dto.CategoryDto;

public interface CategoryAdminService {
    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(long catId, CategoryDto categoryDto);

    void deleteCategoryById(long catId);
}
