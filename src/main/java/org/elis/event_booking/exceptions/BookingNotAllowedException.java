package org.elis.event_booking.exceptions;

public class BookingNotAllowedException extends RuntimeException {

        public BookingNotAllowedException() {
            super("Maximum capacity reached. Reservation not allowed.");
        }
}
