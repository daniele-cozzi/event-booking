package org.elis.event_booking.controller;

import jakarta.validation.Valid;
import org.elis.event_booking.dto.SeatDTO;
import org.elis.event_booking.dto.SeatEditPriceDTO;
import org.elis.event_booking.exceptions.*;
import org.elis.event_booking.model.User;
import org.elis.event_booking.service.definitions.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/seats")
public class SeatController {

    private final SeatService seatService;

    @Autowired
    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @PostMapping
    public ResponseEntity<SeatDTO> saveSeat(@Valid @RequestBody SeatDTO seatDTO) throws EntityNotFoundException, EntityCreationException {
        return new ResponseEntity<>(seatService.save(seatDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SeatDTO>> getSeats() {
        return new ResponseEntity<>(seatService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<SeatDTO> getSeatById(@PathVariable long id) throws EntityNotFoundException {
        return new ResponseEntity<>(seatService.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/name")
    public ResponseEntity<List<SeatDTO>> getSeatByName(@RequestParam String name) {
        return new ResponseEntity<>(seatService.findByName(name), HttpStatus.OK);
    }

    @PatchMapping(path = "/edit-price")
    public ResponseEntity<SeatDTO> editSeatPrice(@Valid @RequestBody SeatEditPriceDTO seatEditPriceDTO) throws EntityNotFoundException, SeatEditPriceNotAllowedException, EntityEditException {
        return new ResponseEntity<>(seatService.editPrice(seatEditPriceDTO), HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<Void> removeSeat(@RequestParam long id) throws EntityNotFoundException, EntityDeletionException {
        seatService.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
