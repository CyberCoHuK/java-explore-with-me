package ru.practicum.ewm_service.compilations.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm_service.compilations.dto.CompilationDto;
import ru.practicum.ewm_service.compilations.dto.NewCompilationDto;
import ru.practicum.ewm_service.compilations.model.Compilation;
import ru.practicum.ewm_service.events.dto.EventDtoShort;
import ru.practicum.ewm_service.events.model.Event;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CompilationMapper {
    public static Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event> events) {
        return Compilation.builder()
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.getPinned())
                .events(events)
                .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilation, List<EventDtoShort> events) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(events)
                .build();
    }
}
