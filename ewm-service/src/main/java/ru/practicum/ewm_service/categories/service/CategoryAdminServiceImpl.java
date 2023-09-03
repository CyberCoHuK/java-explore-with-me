package ru.practicum.ewm_service.categories.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.practicum.ewm_service.categories.dto.CategoryDto;
import ru.practicum.ewm_service.categories.dto.NewCategoryDto;
import ru.practicum.ewm_service.categories.mapper.CategoriesMapper;
import ru.practicum.ewm_service.categories.model.Category;
import ru.practicum.ewm_service.categories.repository.CategoryRepository;
import ru.practicum.ewm_service.events.repository.EventRepository;
import ru.practicum.ewm_service.exceptions.exception.CategoryIsNotEmpty;
import ru.practicum.ewm_service.exceptions.exception.ObjectNotFoundException;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryAdminServiceImpl implements CategoryAdminService {
    private final CategoryRepository repository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto createCategory(NewCategoryDto categoryDto) {
        return CategoriesMapper.toCategoryDto(repository.save(CategoriesMapper.toCategory(categoryDto)));
    }

    @Override
    public CategoryDto updateCategory(long catId, CategoryDto categoryDto) {
        Category update = repository.findById(catId)
                .orElseThrow(() -> new ObjectNotFoundException("Категории с id = " + catId + " не существует"));
        Optional.ofNullable(categoryDto.getName()).ifPresent(update::setName);
        return CategoriesMapper.toCategoryDto(repository.save(update));
    }

    @Override
    public void deleteCategoryById(long catId) {
        Category delete = repository.findById(catId)
                .orElseThrow(() -> new ObjectNotFoundException("Категории с id = " + catId + " не существует"));
        if (!CollectionUtils.isEmpty(eventRepository.findAllByCategoryId(catId))) {
            throw new CategoryIsNotEmpty("С категорией существуют связанные события");
        }
        repository.delete(delete);
    }
}
