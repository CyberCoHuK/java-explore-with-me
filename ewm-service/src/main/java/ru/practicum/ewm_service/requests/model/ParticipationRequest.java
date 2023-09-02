package ru.practicum.ewm_service.requests.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm_service.events.model.Event;
import ru.practicum.ewm_service.user.model.User;
import ru.practicum.ewm_service.utils.Status;

import javax.persistence.*;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "participation_requests")
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    @ManyToOne
    @JoinColumn(name = "event_id")
    Event event;
    LocalDateTime created;
    @ManyToOne
    @JoinColumn(name = "requester")
    User requester;
    @Enumerated(EnumType.STRING)
    Status status;
}
