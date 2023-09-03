package ru.practicum.ewm_service.categories.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_service.categories.dto.CategoryDto;
import ru.practicum.ewm_service.categories.dto.NewCategoryDto;
import ru.practicum.ewm_service.categories.service.CategoryAdminService;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
public class CategoryAdminController {
    private final CategoryAdminService categoryAdminService;

    @PatchMapping
    public CategoryDto createCategory(@Valid @RequestBody NewCategoryDto categoryDto) {
        return categoryAdminService.createCategory(categoryDto);
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(@PathVariable Long catId, @RequestBody CategoryDto categoryDto) {
        return categoryAdminService.updateCategory(catId, categoryDto);
    }

    @DeleteMapping("/{catId}")
    public void deleteCategoryById(@PathVariable long catId) {
        categoryAdminService.deleteCategoryById(catId);
    }
}
