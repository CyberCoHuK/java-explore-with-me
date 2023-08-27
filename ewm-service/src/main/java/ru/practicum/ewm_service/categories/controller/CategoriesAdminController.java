package ru.practicum.ewm_service.categories.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_service.categories.dto.CategoryDto;
import ru.practicum.ewm_service.categories.service.CategoriesAdminService;

@Validated
@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
public class CategoriesAdminController {
    private final CategoriesAdminService categoriesAdminService;

    @PatchMapping
    public CategoryDto createCategory(@RequestBody CategoryDto categoryDto) {
        return categoriesAdminService.createCategory(categoryDto);
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(@PathVariable Long catId, @RequestBody CategoryDto categoryDto) {
        return categoriesAdminService.updateCategory(catId, categoryDto);
    }

    @DeleteMapping("/{catId}")
    public void deleteCategoryById(@PathVariable long catId) {
        categoriesAdminService.deleteCategoryById(catId);
    }
}
