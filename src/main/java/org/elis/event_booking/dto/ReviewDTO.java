package org.elis.event_booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private long id;

    @NotBlank(message = "title cannot be blank")
    @Size(min = 1, max = 30, message = "title cannot be less than 1 character or more than 30 characters long")
    private String title;

    @NotBlank(message = "description cannot be blank")
    @Size(min = 1, max = 255, message = "description cannot be less than 1 character or more than 255 characters long")
    private String description;

    @NotNull(message = "rating cannot be null")
    @Range(min = 0, max = 5, message = "rating must be between 0 and 5")
    private int rating;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime dateTime;

    @Positive(message = "user_id cannot be a negative number")
    @NotNull(message = "user_id cannot be null")
    private long user_id;

    @Positive(message = "eventDate_id cannot be a negative number")
    @NotNull(message = "eventDate_id cannot be null")
    private long eventDate_id;
}
