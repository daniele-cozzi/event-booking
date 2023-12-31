package org.elis.event_booking.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatEditPriceDTO {

    @Positive(message = "id cannot be a negative number")
    @NotNull(message = "id cannot be null")
    private long id;

    @Positive(message = "price cannot be a negative number")
    @NotNull(message = "price cannot be null")
    @Digits(integer = 6, fraction = 2, message = "price should have up to 6 integer digits and 2 decimal places")
    private BigDecimal price;
}
