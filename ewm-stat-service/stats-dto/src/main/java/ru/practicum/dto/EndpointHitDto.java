package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EndpointHitDto {
    @NotNull
    @Size(max = 255)
    private String app;
    @NotNull
    @Size(max = 255)
    private String uri;
    @NotNull
    @Size(max = 30)
    private String ip;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;
}
