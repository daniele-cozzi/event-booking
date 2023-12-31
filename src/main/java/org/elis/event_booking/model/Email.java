package org.elis.event_booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Email {
    private String recipient;
    private String subject;
    private String body;

    // TODO send ticket as attachment (PDF)
    // private String attachment;
}
