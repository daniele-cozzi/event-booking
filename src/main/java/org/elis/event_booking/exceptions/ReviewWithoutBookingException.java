package org.elis.event_booking.exceptions;

public class ReviewWithoutBookingException extends RuntimeException {

    public ReviewWithoutBookingException(long eventDate_id, long user_id) {
        super(String.format("The user with id = %d has no active reservation for the date with id = %d. Review not allowed", user_id, eventDate_id));
    }
}
