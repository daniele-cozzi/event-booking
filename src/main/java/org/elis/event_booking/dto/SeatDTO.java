package org.elis.event_booking.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatDTO {

    private long id;

    @NotBlank(message = "name cannot be blank")
    @Size(min = 1, max = 20, message = "name cannot be less than 1 character or more than 20 characters long")
    private String name;

    @Positive(message = "price cannot be a negative number")
    @NotNull(message = "price cannot be null")
    @Digits(integer = 6, fraction = 2, message = "price should have up to 6 integer digits and 2 decimal places")
    private BigDecimal price;

    @Positive(message = "zone_id cannot be a negative number")
    @NotNull(message = "zone_id cannot be null")
    private long zone_id;
}
