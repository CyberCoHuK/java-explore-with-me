package ru.practicum.ewm_service.statclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.EndpointHitDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;


@Component
@Slf4j
public class Client extends BaseClient {
    @Autowired
    public Client(@Value("${stats-client.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
    }

    public void createStat(HttpServletRequest request) {
        EndpointHitDto endpointHitDto = EndpointHitDto.builder()
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .app("ewm_service")
                .build();
        log.info("Отправлен get запрос на сервер с данными " + endpointHitDto);
        post("/hit", endpointHitDto);
    }

    public Long getView(Long eventId) {
        Map<String, Object> parameters = Map.of(
                "eventId", eventId
        );
        String responseBody = (Objects.requireNonNullElse(get("/view/{eventId}", parameters).getBody(), 0L)).toString();
        return Long.parseLong(responseBody);
    }
}