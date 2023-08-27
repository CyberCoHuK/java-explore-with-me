package ru.practicum.ewm_service.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm_service.events.dto.EventDtoShort;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "compilations")
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {
    private List<EventDtoShort> events;
    private Boolean pinned;
    private String title;
}
