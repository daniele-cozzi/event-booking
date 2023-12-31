package org.elis.event_booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDTO {

    private LocalDateTime timestamp;
    private int status;
    String error;
    // String message or Map<String, String> message
    Object message;
    String path;
}