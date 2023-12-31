package org.elis.event_booking.exceptions;

public class UserNotActiveException extends RuntimeException {

    public UserNotActiveException(String email) {
        super(String.format("User with the email %s is not active.", email));
    }
}
