package ru.practicum.ewm_service.compilations.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_service.compilations.dto.CompilationDto;
import ru.practicum.ewm_service.compilations.dto.NewCompilationDto;
import ru.practicum.ewm_service.compilations.dto.UpdateCompilationRequest;
import ru.practicum.ewm_service.compilations.service.CompilationsAdminService;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
public class CompilationsAdminController {
    private final CompilationsAdminService compilationsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilations(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        return compilationsService.createCompilations(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilationById(@PathVariable Long compId) {
        compilationsService.deleteCompilationById(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto editCompilationById(@PathVariable Long compId,
                                              @RequestBody @Valid UpdateCompilationRequest updateCompilationRequest) {
        return compilationsService.editCompilationById(compId, updateCompilationRequest);
    }
}
