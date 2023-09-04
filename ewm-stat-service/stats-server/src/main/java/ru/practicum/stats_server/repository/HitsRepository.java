package ru.practicum.stats_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.stats_server.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface HitsRepository extends JpaRepository<EndpointHit, Long> {
    @Query("Select new ru.practicum.dto.ViewStatsDto(h.app, h.uri, count(distinct h.ip)) " +
            "from EndpointHit as h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "AND h.uri IN (?3) " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY count(distinct h.ip) desc")
    List<ViewStatsDto> findAllByUriDistinct(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("Select new ru.practicum.dto.ViewStatsDto(h.app, h.uri, count(h.ip)) " +
            "from EndpointHit as h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "AND h.uri IN (?3) " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY count(h.ip) desc")
    List<ViewStatsDto> findAllByUri(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("Select new ru.practicum.dto.ViewStatsDto(h.app, h.uri, count(distinct h.ip)) " +
            "from EndpointHit as h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY count(distinct h.ip) desc")
    List<ViewStatsDto> findAllDistinct(LocalDateTime start, LocalDateTime end);

    @Query("Select new ru.practicum.dto.ViewStatsDto(h.app, h.uri, count(h.ip)) " +
            "from EndpointHit as h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY count(h.ip) desc")
    List<ViewStatsDto> findAll(LocalDateTime start, LocalDateTime end);

    Optional<Long> countDistinctByUri(String s);
}
