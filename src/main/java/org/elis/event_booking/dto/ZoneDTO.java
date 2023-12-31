package org.elis.event_booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZoneDTO {

    private long id;

    @NotBlank(message = "name cannot be blank")
    @Size(min = 1, max = 20, message = "name cannot be less than 1 character or more than 20 characters long")
    private String name;

    @Positive(message = "place_id cannot be a negative number")
    @NotNull(message = "place_id cannot be null")
    private long place_id;
}
