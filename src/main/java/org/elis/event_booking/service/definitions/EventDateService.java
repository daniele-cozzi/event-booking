package org.elis.event_booking.service.definitions;

import org.elis.event_booking.dto.EventDateDTO;

import java.time.LocalDate;
import java.util.List;

public interface EventDateService {
    EventDateDTO save(EventDateDTO eventDateDTO);
    List<EventDateDTO> findAll();
    EventDateDTO findById(long id);
    List<EventDateDTO> findAllByPlaceId(long id);
    List<EventDateDTO> findAllByEventId(long id);
    List<EventDateDTO> findByDate(LocalDate date);
    boolean remove(long id);
}
