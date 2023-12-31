package org.elis.event_booking.service.definitions;

import org.elis.event_booking.dto.CartDTO;

import java.util.List;

public interface CartService {
    List<CartDTO> findAll();
    CartDTO findById(long id);
    CartDTO findByUserId(long id);
    CartDTO addSeat(long user_id, long seat_id);
    boolean removeSeat(long user_id, long seat_id);
}
