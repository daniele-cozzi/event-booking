package org.elis.event_booking.exceptions;

public class EntityEditException extends RuntimeException {

        public EntityEditException(String objectName, String propertyName, long value) {
            super(String.format("There was an error while trying to edit %s with %s = %d.", objectName, propertyName, value));
        }
}
