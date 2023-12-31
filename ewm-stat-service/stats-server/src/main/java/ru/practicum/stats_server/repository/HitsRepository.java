package ru.practicum.stats_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.dto.ViewsDto;
import ru.practicum.stats_server.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
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

    @Query("SELECT COUNT(DISTINCT e.ip) " +
            "FROM EndpointHit AS e " +
            "WHERE e.uri = ?1")
    Long countDistinctByUri(String s);

    @Query("SELECT new ru.practicum.dto.ViewsDto(e.uri, COUNT(DISTINCT e.ip)) " +
            "FROM EndpointHit AS e " +
            "WHERE e.uri IN ?1 " +
            "GROUP BY e.uri")
    List<ViewsDto> countDistinctByUriIn(List<String> uris);
}
