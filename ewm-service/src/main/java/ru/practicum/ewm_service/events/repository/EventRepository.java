package ru.practicum.ewm_service.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm_service.events.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
