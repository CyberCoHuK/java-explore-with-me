package ru.practicum.ewm_service.categories.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_service.categories.dto.CategoryDto;
import ru.practicum.ewm_service.categories.service.CategoriesPublicService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@Validated
@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
public class CategoriesPublicController {
    private final CategoriesPublicService categoriesPublicService;

    @GetMapping
    public Collection<CategoryDto> getCategories(@RequestParam(required = false, defaultValue = "0")
                                                 @PositiveOrZero int from,
                                                 @RequestParam(required = false, defaultValue = "10")
                                                 @Positive int size) {
        return categoriesPublicService.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getById(@PathVariable Long catId) {
        return categoriesPublicService.getCategoryById(catId);
    }
}
