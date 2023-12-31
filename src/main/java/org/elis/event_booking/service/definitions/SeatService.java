package org.elis.event_booking.service.definitions;

import org.elis.event_booking.dto.SeatDTO;
import org.elis.event_booking.dto.SeatEditPriceDTO;

import java.util.List;

public interface SeatService {
    SeatDTO save(SeatDTO seatDTO);
    List<SeatDTO> findAll();
    SeatDTO findById(long id);
    List<SeatDTO> findByName(String name);
    SeatDTO editPrice(SeatEditPriceDTO seatEditPriceDTO);
    boolean remove(long id);
}
