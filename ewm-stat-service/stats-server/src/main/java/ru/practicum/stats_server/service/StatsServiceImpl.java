package ru.practicum.stats_server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.stats_server.mapper.HitsMapper;
import ru.practicum.stats_server.model.EndpointHit;
import ru.practicum.stats_server.repository.HitsRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final HitsRepository hitsRepository;

    @Override
    @Transactional(readOnly = true)
    public Collection<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        List<ViewStatsDto> hits;
        if (uris == null || uris.isEmpty()) {
            if (unique) {
                hits = hitsRepository.findAllDistinct(start, end);
            } else {
                hits = hitsRepository.findAll(start, end);
            }
        } else {
            if (unique) {
                hits = hitsRepository.findAllByUriDistinct(start, end, uris);
            } else {
                hits = hitsRepository.findAllByUri(start, end, uris);
            }
        }

        return hits;
    }

    @Override
    @Transactional
    public void createHit(EndpointHitDto endpointHitDto) {
        EndpointHit saveHit = HitsMapper.toEndpointHit(endpointHitDto);
        hitsRepository.save(saveHit);
    }
}
