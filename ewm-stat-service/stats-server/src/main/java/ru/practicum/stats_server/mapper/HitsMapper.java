package ru.practicum.stats_server.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.stats_server.model.EndpointHit;

@Component
public class HitsMapper {
    public EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                .ip(endpointHitDto.getIp())
                .app(endpointHitDto.getApp())
                .uri(endpointHitDto.getUri())
                .timestamp(endpointHitDto.getTimestamp())
                .build();
    }
}
