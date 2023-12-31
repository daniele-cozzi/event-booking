package org.elis.event_booking.repository;

import org.elis.event_booking.model.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ZoneRepository extends JpaRepository<Zone, Long> {
    List<Zone> findByNameIgnoreCase(String name);
    Optional<Zone> findByNameIgnoreCaseAndPlaceId(String name, long place_id);
}
