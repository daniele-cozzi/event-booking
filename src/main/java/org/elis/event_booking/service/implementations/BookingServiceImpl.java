package org.elis.event_booking.service.implementations;

import jakarta.transaction.Transactional;
import org.elis.event_booking.dto.BookingDTO;
import org.elis.event_booking.dto.mapper.BookingMapper;
import org.elis.event_booking.exceptions.*;
import org.elis.event_booking.model.*;
import org.elis.event_booking.repository.*;
import org.elis.event_booking.service.definitions.BookingService;
import org.elis.event_booking.service.definitions.EmailService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final SeatRepository seatRepository;
    private final EventDateRepository eventDateRepository;
    private final EventRepository eventRepository;
    private final EmailService emailService;

    public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository, SeatRepository seatRepository, EventDateRepository eventDateRepository, EventRepository eventRepository, EmailService emailService) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.seatRepository = seatRepository;
        this.eventDateRepository = eventDateRepository;
        this.eventRepository = eventRepository;
        this.emailService = emailService;
    }

    @Transactional(dontRollbackOn = EmailSendingException.class)
    @Override
    public BookingDTO save(BookingDTO bookingDTO) {
        Booking booking = BookingMapper.INSTANCE.bookingDTOToBooking(bookingDTO);
        User user = userRepository.findById(bookingDTO.getUser_id()).orElseThrow(() -> new EntityNotFoundException("user", "id", bookingDTO.getUser_id()));
        Seat seat = seatRepository.findById(bookingDTO.getSeat_id()).orElseThrow(() -> new EntityNotFoundException("seat", "id", bookingDTO.getSeat_id()));
        EventDate eventDate = eventDateRepository.findById(bookingDTO.getEventDate_id()).orElseThrow(() -> new EntityNotFoundException("eventDate", "id", bookingDTO.getEventDate_id()));

        // I'm looking for a booking with the same seat and the same eventDate, but only if it is active
        Booking tempBooking = bookingRepository.findBySeatAndEventDateAndActiveTrue(seat, eventDate).orElse(null);

        if (tempBooking != null) throw new EntityDuplicateException("booking", "seat_id", bookingDTO.getSeat_id(), "eventDate_id", bookingDTO.getEventDate_id());

        // I check that the place of the booking belongs to the place where the date takes place
        List<Seat> placeSeats = seatRepository.findAllByPlaceId(eventDate.getPlace().getId());

        if (!placeSeats.contains(seat)) throw new DifferentPlaceException(bookingDTO.getEventDate_id(), bookingDTO.getSeat_id());

        // I'm counting active bookings for this eventDate
        long activeBooking = bookingRepository.countByActiveTrueAndEventDate(eventDate);

        if (activeBooking >= eventDate.getMaxAvailable()) throw new BookingNotAllowedException();

        booking.setDate(LocalDateTime.now());
        booking.setUser(user);
        booking.setSeat(seat);
        booking.setEventDate(eventDate);

        try {
            booking = bookingRepository.save(booking);
        } catch (Exception e) {
            throw new EntityCreationException("booking");
        }

        // I send email. If the email is not sent the reservation is still made
        if (!emailService.sendBookingConfirmationEmail(booking)) throw new EmailSendingException("made");

        return BookingMapper.INSTANCE.bookingToBookingDTO(booking);
    }

    @Override
    public List<BookingDTO> findAll() {
        List<Booking> bookings = bookingRepository.findAll();
        return BookingMapper.INSTANCE.bookingsToBookingDTOs(bookings);
    }

    @Override
    public BookingDTO findById(long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("booking", "id", id));
        return BookingMapper.INSTANCE.bookingToBookingDTO(booking);
    }

    @Override
    public List<BookingDTO> findAllByUserId(long id) {
        List<Booking> bookings = bookingRepository.findAllByUserId(id);
        return BookingMapper.INSTANCE.bookingsToBookingDTOs(bookings);
    }

    @Override
    public List<BookingDTO> findAllByEventId(long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("event", "id", id));

        List<Booking> bookings = bookingRepository.findAllByEventId(id);
        return BookingMapper.INSTANCE.bookingsToBookingDTOs(bookings);
    }

    @Transactional(dontRollbackOn = EmailSendingException.class)
    @Override
    public BookingDTO disableById(long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("booking", "id", id));

        // I check if there are less than 20 days left from the eventDate
        LocalDateTime eventDateStart = booking.getEventDate().getStart();
        if (LocalDateTime.now().until(eventDateStart, ChronoUnit.DAYS) < 20) throw new BookingDisableNotAllowedException();

        booking.setActive(false);

        try {
            booking = bookingRepository.save(booking);
        } catch (Exception e) {
            throw new EntityEditException("booking", "id", id);
        }

        // I send email. If the email is not sent the reservation is still disabled
        if (!emailService.sendBookingDisabledEmail(booking)) throw new EmailSendingException("disabled");

        return BookingMapper.INSTANCE.bookingToBookingDTO(booking);
    }

    @Override
    public boolean remove(long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("booking", "id", id));

        try {
            bookingRepository.delete(booking);
        } catch (Exception e) {
            throw new EntityDeletionException("booking", "id", id);
        }

        return true;
    }
}