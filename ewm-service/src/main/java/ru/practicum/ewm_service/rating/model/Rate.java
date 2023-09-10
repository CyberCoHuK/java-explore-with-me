package ru.practicum.ewm_service.rating.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.practicum.ewm_service.events.model.Event;
import ru.practicum.ewm_service.user.model.User;

import javax.persistence.*;
import java.util.Objects;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@Entity
@Table(name = "marks")
@NoArgsConstructor
@AllArgsConstructor
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    User user;
    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    Event event;
    Boolean rate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rate rate = (Rate) o;
        return id.equals(rate.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
