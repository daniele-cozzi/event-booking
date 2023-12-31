package org.elis.event_booking.exceptions;

public class EntityDeletionException extends RuntimeException {

    public EntityDeletionException(String objectName, String propertyName, long value) {
        super(String.format("There was an error while trying to delete %s with %s = %d.", objectName, propertyName, value));
    }
}
