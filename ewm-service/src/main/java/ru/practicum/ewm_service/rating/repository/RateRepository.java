package ru.practicum.ewm_service.rating.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm_service.events.model.Event;
import ru.practicum.ewm_service.rating.dto.RateShortDto;
import ru.practicum.ewm_service.rating.model.Rate;
import ru.practicum.ewm_service.user.model.User;

import java.util.List;
import java.util.Optional;

public interface RateRepository extends JpaRepository<Rate, Long> {
    boolean existsByUserAndEvent(User user, Event event);

    Optional<Rate> findByUserIdAndEventIdAndId(Long userId, Long eventId, Long rateId);

    Long countByEventIdAndRateEquals(Long id, Boolean rate);

    @Query("SELECT new ru.practicum.ewm_service.rating.dto.RateShortDto(r.event.id, count(r.id)) " +
            "FROM Rate AS r " +
            "WHERE r.event.id IN ?1 " +
            "AND r.rate = ?2 " +
            "GROUP BY r.event.id")
    List<RateShortDto> findRate(List<Long> eventsId, Boolean rate);

}
