package org.elis.event_booking.repository;

import org.elis.event_booking.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByNameIgnoreCase(String name);
}
