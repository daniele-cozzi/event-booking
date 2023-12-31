package org.elis.event_booking.service.definitions;

import org.elis.event_booking.dto.ReviewDTO;

import java.util.List;

public interface ReviewService {
    ReviewDTO save(ReviewDTO reviewDTO);
    List<ReviewDTO> findAll();
    ReviewDTO findById(long id);
    List<ReviewDTO> findAllByUserId(long id);
    boolean remove(long id);
}
