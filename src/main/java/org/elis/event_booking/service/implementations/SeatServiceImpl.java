package org.elis.event_booking.service.implementations;

import jakarta.transaction.Transactional;
import org.elis.event_booking.dto.SeatDTO;
import org.elis.event_booking.dto.SeatEditPriceDTO;
import org.elis.event_booking.dto.mapper.SeatMapper;
import org.elis.event_booking.exceptions.*;
import org.elis.event_booking.model.Seat;
import org.elis.event_booking.model.User;
import org.elis.event_booking.model.Zone;
import org.elis.event_booking.repository.*;
import org.elis.event_booking.service.definitions.SeatService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final ZoneRepository zoneRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    public SeatServiceImpl(SeatRepository seatRepository, ZoneRepository zoneRepository, CartRepository cartRepository, UserRepository userRepository, BookingRepository bookingRepository) {
        this.seatRepository = seatRepository;
        this.zoneRepository = zoneRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public SeatDTO save(SeatDTO seatDTO) {
        Seat seat = SeatMapper.INSTANCE.seatDTOToSeat(seatDTO);
        Zone zone = zoneRepository.findById(seat.getZone().getId()).orElseThrow(() -> new EntityNotFoundException("zone", "id", seatDTO.getZone_id()));

        seat.setZone(zone);

        try {
            seat = seatRepository.save(seat);
        } catch (Exception e) {
            throw new EntityCreationException("seat");
        }

        return SeatMapper.INSTANCE.seatToSeatDTO(seat);
    }

    @Override
    public List<SeatDTO> findAll() {
        List<Seat> seats = seatRepository.findAll();
        return SeatMapper.INSTANCE.seatsToSeatDTOs(seats);
    }

    @Override
    public SeatDTO findById(long id) {
        Seat seat = seatRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("seat", "id", id));
        return SeatMapper.INSTANCE.seatToSeatDTO(seat);
    }

    @Override
    public List<SeatDTO> findByName(String name) {
        List<Seat> seats = seatRepository.findByNameIgnoreCase(name);
        return SeatMapper.INSTANCE.seatsToSeatDTOs(seats);
    }

    @Override
    public SeatDTO editPrice(SeatEditPriceDTO seatEditPriceDTO) {
        /*
            TODO Edit the ER diagram.
             The price is not associated with the seat by eventDate,
             so a particular seat has the same price for every eventDate that takes place there
        */

        Seat seat = seatRepository.findById(seatEditPriceDTO.getId()).orElseThrow(() -> new EntityNotFoundException("seat", "id", seatEditPriceDTO.getId()));

        // if the place has active bookings I cannot change the price
        if (!bookingRepository.findBySeatAndActiveTrue(seat).isEmpty()) throw new SeatEditPriceNotAllowedException(seat.getId());

        seat.setPrice(seatEditPriceDTO.getPrice());

        try {
            seat = seatRepository.save(seat);
        } catch (Exception e) {
            throw new EntityEditException("seat", "id", seatEditPriceDTO.getId());
        }

        return SeatMapper.INSTANCE.seatToSeatDTO(seat);
    }

    @Override
    public boolean remove(long id) {
        Seat seat = seatRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("seat", "id", id));

        // I remove the seat from the carts
        seat.getCarts().forEach(c -> c.getSeats().remove(seat));

        cartRepository.saveAll(seat.getCarts());

        try {
            seatRepository.delete(seat);
        } catch (Exception e) {
            throw new EntityDeletionException("seat", "id", id);
        }

        return true;
    }
}