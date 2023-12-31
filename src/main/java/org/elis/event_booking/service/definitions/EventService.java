package org.elis.event_booking.service.definitions;

import org.elis.event_booking.dto.EventDTO;

import java.util.List;

public interface EventService {
    EventDTO save(EventDTO eventDTO);
    List<EventDTO> findAll();
    EventDTO findById(long id);
    EventDTO findByName(String name);
    boolean remove(long id);
}
