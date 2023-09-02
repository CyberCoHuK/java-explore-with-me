package ru.practicum.ewm_service.compilations.service;

import ru.practicum.ewm_service.compilations.dto.CompilationDto;
import ru.practicum.ewm_service.compilations.dto.NewCompilationDto;
import ru.practicum.ewm_service.compilations.dto.UpdateCompilationRequest;

public interface CompilationsAdminService {
    CompilationDto createCompilations(NewCompilationDto newCompilationDto);

    void deleteCompilationById(Long compId);

    CompilationDto editCompilationById(Long compId, UpdateCompilationRequest updateCompilationRequest);
}
