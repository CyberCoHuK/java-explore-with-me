package ru.practicum.ewm_service.categories.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm_service.categories.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
