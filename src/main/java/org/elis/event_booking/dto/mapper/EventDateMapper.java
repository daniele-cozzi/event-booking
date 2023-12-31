package org.elis.event_booking.dto.mapper;

import org.elis.event_booking.dto.EventDateDTO;
import org.elis.event_booking.model.EventDate;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface EventDateMapper {
    EventDateMapper INSTANCE = Mappers.getMapper(EventDateMapper.class);

    @Mapping(source = "event.id", target = "event_id")
    @Mapping(source = "place.id", target = "place_id")
    EventDateDTO eventDateToEventDateDTO(EventDate eventDate);

    List<EventDateDTO> eventDatesToEventDateDTOs(List<EventDate> eventDates);

    @InheritInverseConfiguration
    EventDate eventDateDTOToEventDate(EventDateDTO eventDateDTO);
}
