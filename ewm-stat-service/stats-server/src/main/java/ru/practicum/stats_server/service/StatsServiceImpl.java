package ru.practicum.stats_server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.dto.ViewsDto;
import ru.practicum.stats_server.mapper.HitsMapper;
import ru.practicum.stats_server.repository.HitsRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final HitsRepository hitsRepository;

    @Override
    @Transactional(readOnly = true)
    public Collection<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        return (CollectionUtils.isEmpty(uris)) ?
                (unique ? hitsRepository.findAllDistinct(start, end) : hitsRepository.findAll(start, end)) :
                (unique ? hitsRepository.findAllByUriDistinct(start, end, uris) :
                        hitsRepository.findAllByUri(start, end, uris));
    }

    @Override
    @Transactional
    public void createHit(EndpointHitDto endpointHitDto) {
        hitsRepository.save(HitsMapper.toEndpointHit(endpointHitDto));
    }

    @Override
    @Transactional(readOnly = true)
    public Long getView(long eventId) {
        Long view = hitsRepository.countDistinctByUri("/events/" + eventId);
        return Objects.requireNonNullElse(view, 0L);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewsDto> getViews(List<String> eventsId) {
        return hitsRepository.countDistinctByUriIn(eventsId);
    }
}
