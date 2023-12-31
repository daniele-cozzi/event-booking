package org.elis.event_booking.repository;

import org.elis.event_booking.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByNameIgnoreCase(String name);
    @Query("SELECT s FROM Seat s INNER JOIN Zone z ON s.zone = z INNER JOIN Place p ON z.place = p WHERE p.id = :id")
    List<Seat> findAllByPlaceId(long id);
}
