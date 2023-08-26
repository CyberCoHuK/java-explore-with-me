package ru.practicum.stats_server.mapper;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.stats_server.model.EndpointHit;

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
