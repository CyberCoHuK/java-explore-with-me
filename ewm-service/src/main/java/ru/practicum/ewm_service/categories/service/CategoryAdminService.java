package ru.practicum.ewm_service.categories.service;

import ru.practicum.ewm_service.categories.dto.CategoryDto;
import ru.practicum.ewm_service.categories.dto.NewCategoryDto;

public interface CategoryAdminService {
    CategoryDto createCategory(NewCategoryDto categoryDto);

    CategoryDto updateCategory(long catId, CategoryDto categoryDto);

    void deleteCategoryById(long catId);
}
