package org.elis.event_booking.dto.mapper;

import org.elis.event_booking.dto.BookingDTO;
import org.elis.event_booking.model.Booking;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Mapping(source = "user.id", target = "user_id")
    @Mapping(source = "seat.id", target = "seat_id")
    @Mapping(source = "eventDate.id", target = "eventDate_id")
    BookingDTO bookingToBookingDTO(Booking booking);

    List<BookingDTO> bookingsToBookingDTOs(List<Booking> bookings);

    @InheritInverseConfiguration
    Booking bookingDTOToBooking(BookingDTO bookingDTO);
}
