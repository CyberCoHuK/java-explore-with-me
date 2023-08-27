package ru.practicum.ewm_service.categories.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm_service.categories.dto.CategoryDto;
import ru.practicum.ewm_service.categories.mapper.CategoriesMapper;
import ru.practicum.ewm_service.categories.model.Category;
import ru.practicum.ewm_service.categories.repository.CategoriesRepository;
import ru.practicum.ewm_service.exceptions.ObjectNotFoundException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoriesAdminServiceImpl implements CategoriesAdminService {
    private final CategoriesRepository categoriesRepository;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        return CategoriesMapper.toCategoryDtoR(categoriesRepository.save(CategoriesMapper.toCategory(categoryDto)));
    }

    @Override
    public CategoryDto updateCategory(long catId, CategoryDto categoryDto) {
        Category update = categoriesRepository.findById(catId)
                .orElseThrow(() -> new ObjectNotFoundException("Категории с id = " + catId + " не существует"));
        Optional.ofNullable(categoryDto.getName()).ifPresent(update::setName);
        return CategoriesMapper.toCategoryDtoR(categoriesRepository.save(update));
    }

    @Override
    public void deleteCategoryById(long catId) {
        Category delete = categoriesRepository.findById(catId)
                .orElseThrow(() -> new ObjectNotFoundException("Категории с id = " + catId + " не существует"));
        categoriesRepository.save(delete);
    }
}
