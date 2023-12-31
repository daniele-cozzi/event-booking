package org.elis.event_booking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDTO {

    @Email(message = "email is not a well-formed email address")
    @NotBlank(message = "email cannot be blank")
    @Size(min = 1, max = 50, message = "email cannot be less than 1 character or more than 50 characters long")
    private String email;

    @NotBlank(message = "password cannot be blank")
    @Size(min = 8, max = 100, message = "password cannot be less than 8 character or more than 100 characters long")
    private String password;
}
