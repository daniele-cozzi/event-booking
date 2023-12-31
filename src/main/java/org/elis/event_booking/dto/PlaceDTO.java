package org.elis.event_booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDTO {

    private long id;

    @NotBlank(message = "name cannot be blank")
    @Size(min = 1, max = 30, message = "name cannot be less than 1 character or more than 30 characters long")
    private String name;

    @NotBlank(message = "road cannot be blank")
    @Size(min = 1, max = 30, message = "road cannot be less than 1 character or more than 30 characters long")
    private String road;

    @NotBlank(message = "city cannot be blank")
    @Size(min = 1, max = 20, message = "city cannot be less than 1 character or more than 20 characters long")
    private String city;

    @NotBlank(message = "province cannot be blank")
    @Pattern(regexp = "[A-Z][A-Z]", message = "province should be 2 uppercase letters")
    private String province;

    @NotBlank(message = "postalCode cannot be blank")
    @Pattern(regexp = "^[0-9]{5}$", message = "postalCode should be a 5-digit number")
    private String postalCode;
}
