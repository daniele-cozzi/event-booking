package org.elis.event_booking.controller;

import jakarta.validation.Valid;
import org.elis.event_booking.dto.ReviewDTO;
import org.elis.event_booking.exceptions.*;
import org.elis.event_booking.model.User;
import org.elis.event_booking.service.definitions.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> saveReview(@Valid @RequestBody ReviewDTO reviewDTO)
            throws EntityNotFoundException, ReviewWithoutBookingException, ReviewBeforeEndException, EntityCreationException {
        return new ResponseEntity<>(reviewService.save(reviewDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getReviews() {
        return new ResponseEntity<>(reviewService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable long id) throws EntityNotFoundException {
        return new ResponseEntity<>(reviewService.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/user")
    public ResponseEntity<List<ReviewDTO>> getReviewsByUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<ReviewDTO> reviewDTOs = reviewService.findAllByUserId(user.getId());
        return new ResponseEntity<>(reviewDTOs, HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<Void> removeReview(@RequestParam long id) throws EntityNotFoundException, EntityDeletionException {
        reviewService.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
