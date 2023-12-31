package org.elis.event_booking.service.definitions;

import org.elis.event_booking.dto.BookingDTO;

import java.util.List;

public interface BookingService {
    BookingDTO save(BookingDTO bookingDTO);
    List<BookingDTO> findAll();
    BookingDTO findById(long id);
    List<BookingDTO> findAllByUserId(long id);
    List<BookingDTO> findAllByEventId(long id);
    BookingDTO disableById(long id);
    boolean remove(long id);
}
