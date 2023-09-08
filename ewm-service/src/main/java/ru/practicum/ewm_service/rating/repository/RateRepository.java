package ru.practicum.ewm_service.rating.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm_service.events.model.Event;
import ru.practicum.ewm_service.rating.model.Rate;
import ru.practicum.ewm_service.user.model.User;

import java.util.Optional;

public interface RateRepository extends JpaRepository<Rate, Long> {
    boolean existsByUserAndEvent(User user, Event event);

    Optional<Rate> findByUserIdAndEventIdAndId(Long userId, Long eventId, Long rateId);

    Long countByEventIdAndRateEquals(Long id, Boolean rate);
}
