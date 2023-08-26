package ru.practicum.stats_server.mapper;

import lombok.Builder;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.stats_server.model.EndpointHit;

@Builder
public class HitsMapper {
    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                .ip(endpointHitDto.getIp())
                .app(endpointHitDto.getApp())
                .uri(endpointHitDto.getUri())
                .timestamp(endpointHitDto.getTimestamp())
                .build();
    }
}
