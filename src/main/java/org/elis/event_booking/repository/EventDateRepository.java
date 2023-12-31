package org.elis.event_booking.repository;

import org.elis.event_booking.model.EventDate;
import org.elis.event_booking.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface EventDateRepository extends JpaRepository<EventDate, Long> {
    @Query("SELECT e FROM EventDate e " +
            "WHERE e.place = :place AND ( " +
            "(e.start <= :end AND e.end >= :start) OR " +
            "(e.start >= :start AND e.start < :end) OR " +
            "(e.end > :start AND e.end <= :end) )")
    List<EventDate> checkForDateOverlaps(Place place, LocalDateTime start, LocalDateTime end);

    List<EventDate> findAllByPlaceId(long id);
    List<EventDate> findAllByEventId(long id);
    @Query("SELECT e FROM EventDate e WHERE :date = DATE(e.start) OR :date = DATE(e.end)")
    List<EventDate> findByDate(LocalDate date);
}
