package ru.practicum.ewm_service.compilations.service;

import ru.practicum.ewm_service.compilations.dto.CompilationDto;
import ru.practicum.ewm_service.compilations.dto.NewCompilationDto;

import java.util.Collection;

public interface CompilationsAdminService {
    CompilationDto createCompilations(NewCompilationDto newCompilationDto);

    CompilationDto deleteCompilationById(Long compId);

    CompilationDto editCompilationById(Long compId);
}
