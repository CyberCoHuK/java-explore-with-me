package ru.practicum.ewm_service.compilations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.practicum.ewm_service.compilations.dto.CompilationDto;
import ru.practicum.ewm_service.compilations.dto.NewCompilationDto;
import ru.practicum.ewm_service.compilations.dto.UpdateCompilationRequest;
import ru.practicum.ewm_service.compilations.mapper.CompilationMapper;
import ru.practicum.ewm_service.compilations.model.Compilation;
import ru.practicum.ewm_service.compilations.repository.CompilationRepository;
import ru.practicum.ewm_service.events.model.Event;
import ru.practicum.ewm_service.events.repository.EventRepository;
import ru.practicum.ewm_service.exceptions.exception.ObjectNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CompilationsAdminServiceImpl implements CompilationsAdminService {
    EventRepository eventRepository;
    CompilationRepository compilationRepository;
    CompilationMapper compilationMapper;

    @Override
    public CompilationDto createCompilations(NewCompilationDto newCompilationDto) {
        List<Event> events = eventRepository.findByIdIn(newCompilationDto.getEvents());
        return compilationMapper.toCompilationDto(compilationRepository.save(
                CompilationMapper.toCompilation(newCompilationDto, events)));
    }

    @Override
    public void deleteCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException("Подборки с id = " + compId + " не существует"));
        compilationRepository.delete(compilation);
    }

    @Override
    public CompilationDto editCompilationById(Long compId, UpdateCompilationRequest updateCompilation) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException("Подборки с id = " + compId + " не существует"));
        if (!CollectionUtils.isEmpty(updateCompilation.getEvents())) {
            List<Event> events = eventRepository.findByIdIn(updateCompilation.getEvents());
            compilation.setEvents(events);
        }
        Optional.ofNullable(updateCompilation.getTitle()).ifPresent(compilation::setTitle);
        Optional.ofNullable(updateCompilation.getPinned()).ifPresent(compilation::setPinned);
        return compilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }
}
