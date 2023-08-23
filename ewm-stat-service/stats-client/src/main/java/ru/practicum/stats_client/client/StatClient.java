package ru.practicum.stats_client.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.EndpointHitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StatClient extends BaseClient {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public StatClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
    }

    public ResponseEntity<Object> createStat(EndpointHitDto endpointHitDto) {
        log.info("Отправлен get запрос на сервер с данными " + endpointHitDto);
        return post("/hit", endpointHitDto);
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (start == null || end == null || start.isAfter(end)) {
            throw new IllegalArgumentException("Недопустимый временной промежуток.");
        }
        Map<String, Object> parameters;
        parameters = Map.of(
                "start", start.format(DATE_TIME_FORMATTER),
                "end", end.format(DATE_TIME_FORMATTER),
                "unique", unique
        );
        if (uris != null) {
            parameters = Map.of(
                    "start", start.format(DATE_TIME_FORMATTER),
                    "end", end.format(DATE_TIME_FORMATTER),
                    "uris", String.join(",", uris),
                    "unique", unique
            );
        }
        log.info("Отправлен get запрос на сервер с данными " + parameters);
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }
}