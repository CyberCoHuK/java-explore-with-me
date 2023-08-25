package ru.practicum.stats_server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.stats_server.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class StatsController {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
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
