package ru.practicum.stats_client.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.stats_client.client.StatClient;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.utils.CommonUtils.DATE_FORMAT;

@RestController
@AllArgsConstructor
@Validated
@Slf4j
public class ClientController {
    private StatClient statClient;

    @PostMapping("/hit")
    public ResponseEntity<Object> saveHit(@Valid @RequestBody EndpointHitDto hitDto) {
        log.info("Принят post запрос в клиент с данными" + hitDto);
        return statClient.createStat(hitDto);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(@RequestParam @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime start,
                                           @RequestParam @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime end,
                                           @RequestParam(required = false, name = "uris") List<String> uris,
                                           @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Принят get запрос в клиент с данными start=" + start + " end time=" + end + " uris=" + uris +
                " unique=" + unique);
        return statClient.getStats(start, end, uris, unique);
    }

    @GetMapping("/view/{eventId}")
    public ResponseEntity<Object> getView(@PathVariable long eventId) {
        return statClient.getView(eventId);
    }
}
