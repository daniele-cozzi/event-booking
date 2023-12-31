package org.elis.event_booking.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elis.event_booking.model.Role;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private long id;

    @NotBlank(message = "surname cannot be blank")
    @Size(min = 1, max = 30, message = "surname cannot be less than 1 character or more than 30 characters long")
    private String surname;

    @NotBlank(message = "name cannot be blank")
    @Size(min = 1, max = 30, message = "name cannot be less than 1 character or more than 30 characters long")
    private String name;

    @Email(message = "email is not a well-formed email address")
    @NotBlank(message = "email cannot be blank")
    @Size(min = 1, max = 50, message = "email cannot be less than 1 character or more than 50 characters long")
    private String email;

    @NotBlank(message = "password cannot be blank")
    @Size(min = 8, max = 100, message = "password cannot be less than 8 character or more than 100 characters long")
    private String password;

    @NotNull(message = "birthDate cannot")
    @Past(message = "birthDate cannot be in the future")
    private LocalDate birthDate;

    @NotBlank(message = "fiscalCode cannot be blank")
    @Pattern(
            regexp = "^([A-Z]{6}[0-9LMNPQRSTUV]{2}[ABCDEHLMPRST]{1}[0-9LMNPQRSTUV]{2}[A-Z]{1}[0-9LMNPQRSTUV]{3}[A-Z]{1})$|([0-9]{11})$",
            message = "Invalid fiscalCode format"
    )
    private String fiscalCode;

    private String role;

    private boolean active = true;

    private long cart_id;
}
