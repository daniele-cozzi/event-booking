package org.elis.event_booking.exceptions;

public class BookingDisableNotAllowedException extends RuntimeException {

        public BookingDisableNotAllowedException() {
            super("Disabling the booking is not allowed because there are less than 20 days remaining until the event.");
        }
}
