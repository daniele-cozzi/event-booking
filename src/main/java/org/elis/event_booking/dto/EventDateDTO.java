package org.elis.event_booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDateDTO {

    private long id;

    @NotNull(message = "start cannot be null")
    @Future(message = "start cannot be in the past")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime start;

    @NotNull(message = "end cannot be null")
    @Future(message = "end cannot be in the past")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime end;

    @Positive(message = "maxAvailable cannot be a negative number")
    @NotNull(message = "maxAvailable cannot be null")
    private int maxAvailable;

    @Positive(message = "event_id cannot be a negative number")
    @NotNull(message = "event_id cannot be null")
    private long event_id;

    @Positive(message = "place_id cannot be a negative number")
    @NotNull(message = "place_id cannot be null")
    private long place_id;
}
