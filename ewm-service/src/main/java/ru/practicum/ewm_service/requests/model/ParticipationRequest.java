package ru.practicum.ewm_service.requests.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
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
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    Event event;
    @Column(nullable = false)
    LocalDateTime created;
    @ManyToOne
    @JoinColumn(name = "requester", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    User requester;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Status status;
}
