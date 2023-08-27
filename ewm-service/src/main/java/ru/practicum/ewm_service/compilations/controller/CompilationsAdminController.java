package ru.practicum.ewm_service.compilations.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_service.compilations.dto.CompilationDto;
import ru.practicum.ewm_service.compilations.dto.NewCompilationDto;
import ru.practicum.ewm_service.compilations.service.CompilationsAdminService;

import javax.validation.Valid;
import java.util.Collection;

@Validated
@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
public class CompilationsAdminController {
    private final CompilationsAdminService compilationsService;

    @PostMapping
    public CompilationDto createCompilations(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        return compilationsService.createCompilations(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    public CompilationDto deleteCompilationById(@PathVariable Long compId) {
        return compilationsService.deleteCompilationById(compId);
    }
    @PatchMapping("/{compId}")
    public CompilationDto editCompilationById(@PathVariable Long compId) {
        return compilationsService.editCompilationById(compId);
    }
}
