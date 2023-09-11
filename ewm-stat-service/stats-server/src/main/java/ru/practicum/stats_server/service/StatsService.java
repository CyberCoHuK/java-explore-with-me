package ru.practicum.stats_server.service;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.dto.ViewsDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface StatsService {
    Collection<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

    void createHit(EndpointHitDto endpointHitDto);

    Long getView(long eventId);

    List<ViewsDto> getViews(List<String> eventsId);
}
