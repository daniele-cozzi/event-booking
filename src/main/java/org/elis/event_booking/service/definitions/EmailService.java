package org.elis.event_booking.service.definitions;

import org.elis.event_booking.model.Booking;

public interface EmailService {
    boolean sendBookingConfirmationEmail(Booking booking);
    boolean sendBookingDisabledEmail(Booking booking);
}
