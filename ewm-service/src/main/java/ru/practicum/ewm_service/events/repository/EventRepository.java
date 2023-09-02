package ru.practicum.ewm_service.events.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm_service.events.model.Event;
import ru.practicum.ewm_service.user.model.User;
import ru.practicum.ewm_service.utils.State;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByCategoryId(Long catId);

    List<Event> findByIdIn(List<Long> events);

    @Query("Select e " +
            "FROM Event AS e " +
            "WHERE (?1 is null or e.initiator.id IN (?1)) " +
            "AND (?2 is null or e.state IN (?2)) " +
            "AND (?3 is null or e.categories IN (?3)) " +
            "AND (?4 is null or e.eventDate >= ?4 " +
            "AND (?5 is null or e.eventDate <= ?5")
    List<Event> findAllAdminByData(List<Long> users, List<State> states, List<Long> categories,
                                   LocalDateTime rangeStart, LocalDateTime rangeEnd, PageRequest page);

    Collection<Event> findAllByUser(User user, PageRequest page);

    Collection<Event> findAllByAnnotationOrDescriptionContainingIgnoreCaseAndCategoryIdInAndPaidEqualsAndEventDateAfterAndEventDateBeforeAndStateEquals(String text, String text1, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, State published, PageRequest page);

    Collection<Event> findAllByAnnotationOrDescriptionContainingIgnoreCaseAndCategoryIdInAndPaidEqualsAndEventDateAfterAndStateEquals(String text, String text1, List<Long> categories, Boolean paid, LocalDateTime now, State published, PageRequest page);
}
