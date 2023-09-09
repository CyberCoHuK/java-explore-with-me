package ru.practicum.ewm_service.compilations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm_service.compilations.dto.CompilationDto;
import ru.practicum.ewm_service.compilations.mapper.CompilationMapper;
import ru.practicum.ewm_service.compilations.model.Compilation;
import ru.practicum.ewm_service.compilations.repository.CompilationRepository;
import ru.practicum.ewm_service.events.dto.EventDtoShort;
import ru.practicum.ewm_service.events.mapper.EventMapper;
import ru.practicum.ewm_service.events.model.Event;
import ru.practicum.ewm_service.exceptions.exception.ObjectNotFoundException;
import ru.practicum.ewm_service.requests.repository.RequestRepository;
import ru.practicum.ewm_service.statclient.Client;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompilationsPublicServiceImpl implements CompilationsPublicService {
    private final CompilationRepository compilationRepository;
    private final Client statClient;
    private final RequestRepository requestRepository;

    @Override
    public Collection<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        PageRequest page = PageRequest.of(from / size, size);
        if (pinned != null) {
            return compilationRepository.findAllByPinned(pinned, page).stream()
                    .map(c -> CompilationMapper.toCompilationDto(c, getDto(c.getEvents())))
                    .collect(Collectors.toList());
        }
        return compilationRepository.findAll(page).stream()
                .map(c -> CompilationMapper.toCompilationDto(c, getDto(c.getEvents())))
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException("Подборки с id = " + compId + " не существует"));
        return CompilationMapper.toCompilationDto(compilation, getDto(compilation.getEvents()));
    }

    private List<EventDtoShort> getDto(List<Event> events) {
        List<EventDtoShort> eventDtoShort = events.stream()
                .map(EventMapper::toEventDtoShort)
                .collect(Collectors.toList());
        eventDtoShort.forEach(e -> e.setConfirmedRequests(requestRepository.findConfirmedRequests(e.getId())));
        eventDtoShort.forEach(e -> e.setViews(statClient.getView(e.getId())));
        return eventDtoShort;
    }
}
