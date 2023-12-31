package org.elis.event_booking.service.implementations;

import jakarta.transaction.Transactional;
import org.elis.event_booking.dto.ReviewDTO;
import org.elis.event_booking.dto.mapper.ReviewMapper;
import org.elis.event_booking.exceptions.*;
import org.elis.event_booking.model.EventDate;
import org.elis.event_booking.model.Review;
import org.elis.event_booking.model.User;
import org.elis.event_booking.repository.EventDateRepository;
import org.elis.event_booking.repository.ReviewRepository;
import org.elis.event_booking.repository.UserRepository;
import org.elis.event_booking.service.definitions.ReviewService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final EventDateRepository eventDateRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, UserRepository userRepository, EventDateRepository eventDateRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.eventDateRepository = eventDateRepository;
    }

    @Override
    public ReviewDTO save(ReviewDTO reviewDTO) {
        Review review = ReviewMapper.INSTANCE.reviewDTOToReview(reviewDTO);

        User user = userRepository.findById(reviewDTO.getUser_id()).orElseThrow(() -> new EntityNotFoundException("user", "id", reviewDTO.getUser_id()));
        EventDate eventDate = eventDateRepository.findById(reviewDTO.getEventDate_id()).orElseThrow(() -> new EntityNotFoundException("eventDate", "id", reviewDTO.getEventDate_id()));

        // I check if the user has an active reservation for this evenDate
        List<EventDate> userEventDates = new ArrayList<>();
        user.getBookings().forEach(booking -> {
            if (booking.isActive()) userEventDates.add(booking.getEventDate());
        });

        if (!userEventDates.contains(eventDate)) throw new ReviewWithoutBookingException(eventDate.getId(), user.getId());

        if (LocalDateTime.now().isBefore(eventDate.getEnd())) throw new ReviewBeforeEndException(eventDate.getId());

        review.setDateTime(LocalDateTime.now());
        review.setUser(user);
        review.setEventDate(eventDate);

        try {
            review = reviewRepository.save(review);
        } catch (Exception e) {
            throw new EntityCreationException("review");
        }

        return ReviewMapper.INSTANCE.reviewToReviewDTO(review);
    }

    @Override
    public List<ReviewDTO> findAll() {
        List<Review> reviews = reviewRepository.findAll();
        return ReviewMapper.INSTANCE.reviewsToReviewDTOs(reviews);
    }

    @Override
    public ReviewDTO findById(long id) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("review", "id", id));
        return ReviewMapper.INSTANCE.reviewToReviewDTO(review);
    }

    @Override
    public List<ReviewDTO> findAllByUserId(long id) {
        List<Review> reviews = reviewRepository.findAllByUserId(id);
        return ReviewMapper.INSTANCE.reviewsToReviewDTOs(reviews);
    }

    @Override
    public boolean remove(long id) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("review", "id", id));

        try {
            reviewRepository.delete(review);
        } catch (Exception e) {
            throw new EntityDeletionException("review", "id", id);
        }

        return true;
    }
}
