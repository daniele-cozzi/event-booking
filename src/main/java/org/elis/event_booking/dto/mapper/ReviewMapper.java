package org.elis.event_booking.dto.mapper;

import org.elis.event_booking.dto.ReviewDTO;
import org.elis.event_booking.model.Review;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ReviewMapper {
    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

    @Mapping(source = "user.id", target = "user_id")
    @Mapping(source = "eventDate.id", target = "eventDate_id")
    ReviewDTO reviewToReviewDTO(Review review);

    List<ReviewDTO> reviewsToReviewDTOs(List<Review> reviews);

    @InheritInverseConfiguration
    Review reviewDTOToReview(ReviewDTO reviewDTO);
}
