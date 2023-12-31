package org.elis.event_booking.dto.mapper;

import org.elis.event_booking.dto.EventDTO;
import org.elis.event_booking.model.Event;
import org.mapstruct.factory.Mappers;

import java.util.List;

public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    EventDTO eventToEventDTO(Event event);

    List<EventDTO> eventsToEventDTOs(List<Event> events);

    Event eventDTOToEvent(EventDTO eventDTO);
}
