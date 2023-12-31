package org.elis.event_booking.repository;

import org.elis.event_booking.model.Booking;
import org.elis.event_booking.model.EventDate;
import org.elis.event_booking.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByUserId(long id);
    Optional<Booking> findBySeatAndEventDateAndActiveTrue(Seat seat, EventDate eventDate);
    long countByActiveTrueAndEventDate(EventDate eventDate);
    @Query("SELECT b FROM Booking b INNER JOIN EventDate e ON b.eventDate = e INNER JOIN Event ev ON e.event = ev WHERE ev.id = :id")
    List<Booking> findAllByEventId(long id);
    List<Booking> findBySeatAndActiveTrue(Seat seat);
}
