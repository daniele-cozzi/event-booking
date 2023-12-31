package org.elis.event_booking.exceptions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventDateOverlappingException extends RuntimeException {

    public EventDateOverlappingException(LocalDateTime start, LocalDateTime end, long place_id) {
        super(String.format("The provided eventDate range (start = %s, end = %s) overlaps with an existing eventDate at place with id = %d.",
                start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                place_id));
    }
}
