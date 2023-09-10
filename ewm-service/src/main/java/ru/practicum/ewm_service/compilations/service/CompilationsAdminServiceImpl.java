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
import ru.practicum.ewm_service.events.dto.EventDtoShort;
import ru.practicum.ewm_service.events.mapper.EventMapper;
import ru.practicum.ewm_service.events.model.Event;
import ru.practicum.ewm_service.events.repository.EventRepository;
import ru.practicum.ewm_service.exceptions.exception.ObjectNotFoundException;
import ru.practicum.ewm_service.requests.repository.RequestRepository;
import ru.practicum.ewm_service.statclient.Client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CompilationsAdminServiceImpl implements CompilationsAdminService {
    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;
    private final Client statClient;
    private final RequestRepository requestRepository;

    @Override
    public CompilationDto createCompilations(NewCompilationDto newCompilationDto) {
        List<Event> events = eventRepository.findByIdIn(newCompilationDto.getEvents());
        Compilation compilation = compilationRepository.save(CompilationMapper.toCompilation(newCompilationDto, events));
        return CompilationMapper.toCompilationDto(compilation, getDto(events));
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
        List<Event> events = compilation.getEvents();
        if (!CollectionUtils.isEmpty(updateCompilation.getEvents())) {
            events = eventRepository.findByIdIn(updateCompilation.getEvents());
            compilation.setEvents(events);
        }
        Optional.ofNullable(updateCompilation.getTitle()).ifPresent(compilation::setTitle);
        Optional.ofNullable(updateCompilation.getPinned()).ifPresent(compilation::setPinned);
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation), getDto(events));
    }

    private List<EventDtoShort> getDto(List<Event> events) {
        List<Long> eventsId = events.stream().map(Event::getId).collect(Collectors.toList());
        Map<Long, Long> requests = new HashMap<>();
        Map<Long, Long> views = new HashMap<>();
        requestRepository.findConfirmedRequests(eventsId)
                .forEach(stat -> requests.put(stat.getEventId(), stat.getConfirmedRequests()));
        statClient.getViews(eventsId).
                forEach(view -> views.put(Long.parseLong(view.getEventUri().split("/", 0)[2]), view.getView()));
        return events.stream().map(event -> EventMapper.toEventDtoShort(
                        event,
                        requests.getOrDefault(event.getId(), 0L),
                        views.getOrDefault(event.getId(), 0L)
                ))
                .collect(Collectors.toList());
    }
}
