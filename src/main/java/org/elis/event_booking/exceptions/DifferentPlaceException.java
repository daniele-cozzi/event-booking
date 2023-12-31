package org.elis.event_booking.exceptions;

public class DifferentPlaceException extends RuntimeException {

    public DifferentPlaceException(Long eventDate_id, Long seat_id) {
        super("The event date with id = " + eventDate_id + " and seat with id = " + seat_id + " belong to different places");
    }
}
