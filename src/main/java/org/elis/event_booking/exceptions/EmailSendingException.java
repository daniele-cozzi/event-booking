package org.elis.event_booking.exceptions;

public class EmailSendingException extends RuntimeException {

        public EmailSendingException(String action) {
            super(String.format("Reservation %s but email could not be sent", action));
        }
}
