package org.elis.event_booking.exceptions;

public class ReviewBeforeEndException extends RuntimeException {

    public ReviewBeforeEndException(long eventDate_id) {
        super(String.format("You cannot review the eventDate with id = %d because it is not finished yet.", eventDate_id));
    }
}
