package org.elis.event_booking.controller;

import jakarta.validation.Valid;
import org.elis.event_booking.dto.BookingDTO;
import org.elis.event_booking.exceptions.*;
import org.elis.event_booking.model.User;
import org.elis.event_booking.service.definitions.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingDTO> saveBooking(@Valid @RequestBody BookingDTO bookingDTO)
            throws EntityNotFoundException, EntityDuplicateException, DifferentPlaceException, BookingNotAllowedException, EntityCreationException, EmailSendingException {
        return new ResponseEntity<>(bookingService.save(bookingDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BookingDTO>> getBookings() {
        return new ResponseEntity<>(bookingService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable long id) throws EntityNotFoundException {
        return new ResponseEntity<>(bookingService.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/user")
    public ResponseEntity<List<BookingDTO>> getBookingsByUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<BookingDTO> bookingDTOs = bookingService.findAllByUserId(user.getId());
        return new ResponseEntity<>(bookingDTOs, HttpStatus.OK);
    }

    @GetMapping(path = "/event")
    public ResponseEntity<List<BookingDTO>> getBookingsByEventId(@RequestParam long id) throws EntityNotFoundException {
        return new ResponseEntity<>(bookingService.findAllByEventId(id), HttpStatus.OK);
    }

    @PatchMapping(path = "/disable")
    public ResponseEntity<BookingDTO> disableBooking(@RequestParam long id) throws EntityNotFoundException, BookingDisableNotAllowedException, EntityEditException {
        return new ResponseEntity<>(bookingService.disableById(id), HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<Void> removeBooking(@RequestParam long id) throws EntityNotFoundException, EntityDeletionException {
        bookingService.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}