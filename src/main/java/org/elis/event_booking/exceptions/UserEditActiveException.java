package org.elis.event_booking.exceptions;

public class UserEditActiveException extends RuntimeException {
    public UserEditActiveException(String action, String propertyName, long value) {
        super(String.format("There was an error while trying to %s user with %s = %d.", action, propertyName, value));
    }
}
