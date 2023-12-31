package org.elis.event_booking.controller;

import jakarta.validation.Valid;
import org.elis.event_booking.dto.EventDTO;
import org.elis.event_booking.exceptions.EntityCreationException;
import org.elis.event_booking.exceptions.EntityDeletionException;
import org.elis.event_booking.exceptions.EntityDuplicateException;
import org.elis.event_booking.exceptions.EntityNotFoundException;
import org.elis.event_booking.service.definitions.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/events")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<EventDTO> saveEvent(@Valid @RequestBody EventDTO eventDTO) throws EntityDuplicateException, EntityCreationException {
        return new ResponseEntity<>(eventService.save(eventDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EventDTO>> getEvents() {
        return new ResponseEntity<>(eventService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable long id) throws EntityNotFoundException {
        return new ResponseEntity<>(eventService.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/name")
    public ResponseEntity<EventDTO> getEventByName(@RequestParam String name) throws EntityNotFoundException {
        return new ResponseEntity<>(eventService.findByName(name), HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<Void> removeEvent(@RequestParam long id) throws EntityNotFoundException, EntityDeletionException {
        eventService.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}