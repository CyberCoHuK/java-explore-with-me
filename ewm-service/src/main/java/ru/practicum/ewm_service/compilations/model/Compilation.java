package ru.practicum.ewm_service.compilations.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm_service.events.model.Event;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "compilations")
@NoArgsConstructor
@AllArgsConstructor
public class Compilation {
    private Long id;
    private List<Event> events;
    private Boolean pinned;
    private String title;
}
