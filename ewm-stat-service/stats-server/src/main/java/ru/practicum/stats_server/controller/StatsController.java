package ru.practicum.stats_server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.stats_server.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static ru.practicum.utils.CommonUtils.DATE_FORMAT;

@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @GetMapping("/stats")
    public Collection<ViewStatsDto> getStats(@RequestParam @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime start,
                                             @RequestParam @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime end,
                                             @RequestParam(required = false) List<String> uris,
                                             @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        return statsService.getStats(start, end, uris, unique);
    }

    @PostMapping("/hit")
    public void createHit(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        statsService.createHit(endpointHitDto);
    }
}
