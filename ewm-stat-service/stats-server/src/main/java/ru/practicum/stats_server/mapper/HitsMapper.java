package ru.practicum.stats_server.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.stats_server.model.EndpointHit;

@Component
public class HitsMapper {
    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                .ip(endpointHitDto.getIp())
                .app(endpointHitDto.getApp())
                .uri(endpointHitDto.getUri())
                .timestamp(endpointHitDto.getTimestamp())
                .build();
    }

    public static ViewStatsDto toViewDto(EndpointHit endpointHit) {
        return ViewStatsDto.builder()
                .hits(endpointHit.getId())
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .build();
    }
}
