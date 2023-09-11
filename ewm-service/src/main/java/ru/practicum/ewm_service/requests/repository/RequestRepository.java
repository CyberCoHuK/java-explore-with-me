package ru.practicum.ewm_service.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm_service.events.model.Event;
import ru.practicum.ewm_service.requests.dto.RequestStats;
import ru.practicum.ewm_service.requests.model.ParticipationRequest;
import ru.practicum.ewm_service.user.model.User;
import ru.practicum.ewm_service.utils.Status;

import java.util.Collection;
import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByIdIn(List<Long> requestIds);

    List<ParticipationRequest> findAllByRequester(User user);

    boolean existsByRequesterAndEvent(User user, Event event);

    @Query("SELECT count(PR.id) FROM ParticipationRequest AS PR " +
            "WHERE PR.event.id in :eventId " +
            "AND PR.status = 'CONFIRMED' ")
    Long findConfirmedRequest(Long eventId);

    @Query("SELECT new ru.practicum.ewm_service.requests.dto.RequestStats(PR.event.id, count(PR.id)) " +
            "FROM ParticipationRequest AS PR " +
            "WHERE PR.event.id IN :eventsId " +
            "AND PR.status = 'CONFIRMED' " +
            "GROUP BY PR.event.id")
    List<RequestStats> findConfirmedRequests(List<Long> eventsId);

    Collection<ParticipationRequest> findAllByEvent(Event event);

    boolean existsByEventAndRequesterAndStatus(Event event, User user, Status confirmed);
}
