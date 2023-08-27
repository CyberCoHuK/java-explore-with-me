package ru.practicum.ewm_service.compilations.service;

import ru.practicum.ewm_service.compilations.dto.CompilationDto;

import java.util.Collection;

public class CompilationsPublicServiceImpl implements CompilationsPublicService{
    @Override
    public Collection<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        return null;
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        return null;
    }
}
