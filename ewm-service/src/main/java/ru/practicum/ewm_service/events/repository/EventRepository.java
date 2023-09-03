package ru.practicum.ewm_service.events.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT e FROM Event e " +
            "WHERE (COALESCE(:users, NULL) IS NULL OR e.initiator.id IN :users) " +
            "AND (COALESCE(:states, NULL) IS NULL OR e.state IN :states) " +
            "AND (COALESCE(:categories, NULL) IS NULL OR e.category.id IN :categories) " +
            "AND (COALESCE(:rangeStart, NULL) IS NULL OR e.eventDate >= :rangeStart) " +
            "AND (COALESCE(:rangeEnd, NULL) IS NULL OR e.eventDate <= :rangeEnd) ")
    List<Event> findAllAdminByData(@Param("users") List<Long> users,
                                   @Param("states") List<State> states,
                                   @Param("categories") List<Long> categories,
                                   @Param("rangeStart") LocalDateTime rangeStart,
                                   @Param("rangeEnd") LocalDateTime rangeEnd, PageRequest page);

    Collection<Event> findAllByUser(User user, PageRequest page);

    Collection<Event> findAllByAnnotationOrDescriptionContainingIgnoreCaseAndCategoryIdInAndPaidEqualsAndEventDateAfterAndEventDateBeforeAndStateEquals(String text, String text1, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, State published, PageRequest page);

    Collection<Event> findAllByAnnotationOrDescriptionContainingIgnoreCaseAndCategoryIdInAndPaidEqualsAndEventDateAfterAndStateEquals(String text, String text1, List<Long> categories, Boolean paid, LocalDateTime now, State published, PageRequest page);
}
