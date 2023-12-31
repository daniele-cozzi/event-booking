package org.elis.event_booking.controller;

import jakarta.validation.Valid;
import org.elis.event_booking.dto.EventDateDTO;
import org.elis.event_booking.exceptions.EntityCreationException;
import org.elis.event_booking.exceptions.EntityDeletionException;
import org.elis.event_booking.exceptions.EntityNotFoundException;
import org.elis.event_booking.exceptions.EventDateOverlappingException;
import org.elis.event_booking.service.definitions.EventDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/event-dates")
public class EventDateController {

    private final EventDateService eventDateService;

    @Autowired
    public EventDateController(EventDateService eventDateService) {
        this.eventDateService = eventDateService;
    }

    @PostMapping
    public ResponseEntity<EventDateDTO> saveEventDate(@Valid @RequestBody EventDateDTO eventDateDTO)
            throws EntityNotFoundException, EventDateOverlappingException, IllegalArgumentException, EntityCreationException {
        return new ResponseEntity<>(eventDateService.save(eventDateDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EventDateDTO>> getEventDates() {
        return new ResponseEntity<>(eventDateService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<EventDateDTO> getEventDateById(@PathVariable long id) throws EntityNotFoundException {
        return new ResponseEntity<>(eventDateService.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/place")
    public ResponseEntity<List<EventDateDTO>> getEventDatesByPlaceId(@RequestParam long id) throws EntityNotFoundException {
        return new ResponseEntity<>(eventDateService.findAllByPlaceId(id), HttpStatus.OK);
    }

    @GetMapping(path = "/event")
    public ResponseEntity<List<EventDateDTO>> getEventDatesByEventId(@RequestParam long id) throws EntityNotFoundException {
        return new ResponseEntity<>(eventDateService.findAllByEventId(id), HttpStatus.OK);
    }

    @GetMapping(path = "/date")
    public ResponseEntity<List<EventDateDTO>> getEventDatesByDate(@RequestParam LocalDate date) {
        return new ResponseEntity<>(eventDateService.findByDate(date), HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<Void> removeEventDate(@RequestParam long id) throws EntityNotFoundException, EntityDeletionException {
        eventDateService.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
