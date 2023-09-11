package ru.practicum.ewm_service.events.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.practicum.ewm_service.categories.model.Category;
import ru.practicum.ewm_service.user.model.User;
import ru.practicum.ewm_service.utils.State;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@Entity
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String annotation;
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    Category category;
    @Column(name = "created_on")
    LocalDateTime createdOn;
    String description;
    @Column(name = "event_date")
    LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "initiator_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    User initiator;
    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    Location location;
    Boolean paid;
    @Column(name = "participant_limit")
    Long participantLimit;
    @Column(name = "published_on")
    LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    State state;
    String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id.equals(event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
