package ru.practicum.stats_client.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.stats_client.client.StatClient;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@Validated
@Slf4j
public class ClientController {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private StatClient statClient;

    @PostMapping("/hit")
    public ResponseEntity<Object> saveHit(@Valid @RequestBody EndpointHitDto hitDto) {
        log.info("Принят post запрос в клиент с данными" + hitDto);
        statClient.createStat(hitDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(@RequestParam @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime start,
                                           @RequestParam @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime end,
                                           @RequestParam(required = false, name = "uris") List<String> uris,
                                           @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        log.info("Принят get запрос в клиент с данными start=" + start + " end time=" + end + " uris=" + uris +
                " unique=" + unique);
        return statClient.getStats(start, end, uris, unique);
    }
}
