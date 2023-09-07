package ru.practicum.ewm_service.categories.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm_service.categories.dto.CategoryDto;
import ru.practicum.ewm_service.categories.mapper.CategoriesMapper;
import ru.practicum.ewm_service.categories.model.Category;
import ru.practicum.ewm_service.categories.repository.CategoryRepository;
import ru.practicum.ewm_service.exceptions.exception.ObjectNotFoundException;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryPublicServiceImpl implements CategoryPublicService {
    private final CategoryRepository repository;

    @Override
    public Collection<CategoryDto> getCategories(int from, int size) {
        PageRequest page = PageRequest.of(from / size, size);
        return repository.findAll(page).stream()
                .map(CategoriesMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        Category answer = repository.findById(catId)
                .orElseThrow(() -> new ObjectNotFoundException("Категория с id " + catId + " не найдена"));
        return CategoriesMapper.toCategoryDto(answer);
    }
}
