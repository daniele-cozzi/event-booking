package org.elis.event_booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {

    private long id;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime date;

    private boolean active = true;

    @Positive(message = "user_id cannot be a negative number")
    @NotNull(message = "user_id cannot be null")
    private long user_id;

    @Positive(message = "seat_id cannot be a negative number")
    @NotNull(message = "seat_id cannot be null")
    private long seat_id;

    @Positive(message = "eventDate_id cannot be a negative number")
    @NotNull(message = "eventDate_id cannot be null")
    private long eventDate_id;
}
