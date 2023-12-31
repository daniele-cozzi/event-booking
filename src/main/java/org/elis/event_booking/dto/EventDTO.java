package org.elis.event_booking.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {

    private long id;

    @NotBlank(message = "message cannot be blank")
    @Size(min = 1, max = 50, message = "name cannot be less than 1 character or more than 50 characters long")
    private String name;

    @NotBlank(message = "description cannot be blank")
    private String description;

    private List<String> categories = new ArrayList<>();
}
