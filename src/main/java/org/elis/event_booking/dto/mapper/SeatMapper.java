package org.elis.event_booking.dto.mapper;

import org.elis.event_booking.dto.SeatDTO;
import org.elis.event_booking.model.Seat;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SeatMapper {
    SeatMapper INSTANCE = Mappers.getMapper(SeatMapper.class);

    @Mapping(source = "zone.id", target = "zone_id")
    SeatDTO seatToSeatDTO(Seat seat);

    List<SeatDTO> seatsToSeatDTOs(List<Seat> seats);

    @InheritInverseConfiguration
    Seat seatDTOToSeat(SeatDTO seatDTO);
}
