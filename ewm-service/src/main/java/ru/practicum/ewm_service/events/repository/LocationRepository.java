package ru.practicum.ewm_service.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm_service.events.model.Location;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByLatAndLon(Float lat, Float lat1);
}
