package ru.practicum.ewm_service.compilations.service;

import ru.practicum.ewm_service.compilations.dto.CompilationDto;

import java.util.Collection;

public interface CompilationsPublicService {
    Collection<CompilationDto> getCompilations(Boolean pinned, int from, int size);

    CompilationDto getCompilationById(Long compId);
}
