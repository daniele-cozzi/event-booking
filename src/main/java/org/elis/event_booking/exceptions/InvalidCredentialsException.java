package org.elis.event_booking.exceptions;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Authentication failed: the credentials provided are invalid.");
    }
}
