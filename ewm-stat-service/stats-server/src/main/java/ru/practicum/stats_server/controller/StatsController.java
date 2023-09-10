package ru.practicum.stats_server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.dto.ViewsDto;
import ru.practicum.stats_server.service.StatsService;

import javax.validation.Valid;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @GetMapping("/stats")
    public Collection<ViewStatsDto> getStats(@RequestParam String start,
                                             @RequestParam String end,
                                             @RequestParam(required = false) List<String> uris,
                                             @RequestParam(defaultValue = "false") Boolean unique) {
        LocalDateTime startTime = LocalDateTime.parse(URLDecoder.decode(start, Charset.defaultCharset()));
        LocalDateTime endTime = LocalDateTime.parse(URLDecoder.decode(end, Charset.defaultCharset()));
        return statsService.getStats(startTime, endTime, uris, unique);
    }

    @PostMapping(value = "/hit", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.CREATED)
    public void createHit(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        statsService.createHit(endpointHitDto);
    }

    @GetMapping("/view/{eventId}")
    public Long getView(@PathVariable long eventId) {
        return statsService.getView(eventId);
    }

    @GetMapping("/views")
    public List<ViewsDto> getViews(@RequestParam List<String> eventsId) {
        return statsService.getViews(eventsId);
    }


}
