package org.elis.event_booking.repository;

import org.elis.event_booking.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
     List<Place> findByNameIgnoreCase(String name);
}
