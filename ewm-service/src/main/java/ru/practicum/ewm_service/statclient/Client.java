package ru.practicum.ewm_service.statclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewsDto;
import ru.practicum.ewm_service.exceptions.exception.BadRequestException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Component
@Slf4j
public class Client extends BaseClient {
    private final ObjectMapper mapper = new ObjectMapper();

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

    public List<ViewsDto> getViews(List<Long> eventsId) {
        List<String> uris = eventsId.stream().map((id) -> "/events/" + id).collect(Collectors.toList());
        Map<String, Object> parameters = Map.of(
                "eventsId", String.join(",", uris)
        );
        ResponseEntity<Object> response = get("/views?eventsId={eventsId}", parameters);
        try {
            return Arrays.asList(mapper.readValue(mapper.writeValueAsString(response.getBody()), ViewsDto[].class));
        } catch (JsonProcessingException e) {
            throw new BadRequestException("Не верное значение в при передачи в статистику");
        }
    }
}