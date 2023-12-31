package org.elis.event_booking.exceptions;

public class SeatEditPriceNotAllowedException extends RuntimeException {

    public SeatEditPriceNotAllowedException(long seat_id) {
        super(String.format("The price of the seat with id = %d cannot be edited. There is already an active reservation for it.", seat_id));
    }
}
