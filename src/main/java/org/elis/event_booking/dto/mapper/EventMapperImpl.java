package org.elis.event_booking.dto.mapper;

import java.util.ArrayList;
import java.util.List;
import org.elis.event_booking.dto.EventDTO;
import org.elis.event_booking.model.Event;
import org.elis.event_booking.model.Category;

public class EventMapperImpl implements EventMapper {

    @Override
    public EventDTO eventToEventDTO(Event event) {
        if ( event == null ) {
            return null;
        }

        EventDTO eventDTO = new EventDTO();

        eventDTO.setId( event.getId() );
        eventDTO.setName( event.getName() );
        eventDTO.setDescription( event.getDescription() );

        // I add the categories to the eventDTO
        eventDTO.setCategories(
                event.getCategories().stream().map(Category::getName).toList()
        );

        return eventDTO;
    }

    @Override
    public List<EventDTO> eventsToEventDTOs(List<Event> events) {
        if ( events == null ) {
            return null;
        }

        List<EventDTO> list = new ArrayList<EventDTO>( events.size() );
        for ( Event event : events ) {
            list.add( eventToEventDTO( event ) );
        }

        return list;
    }

    @Override
    public Event eventDTOToEvent(EventDTO eventDTO) {
        if ( eventDTO == null ) {
            return null;
        }

        Event event = new Event();

        event.setId( eventDTO.getId() );
        event.setName( eventDTO.getName() );
        event.setDescription( eventDTO.getDescription() );

        return event;
    }
}
