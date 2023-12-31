package org.elis.event_booking.controller;

import jakarta.validation.Valid;
import org.elis.event_booking.dto.PlaceDTO;
import org.elis.event_booking.exceptions.EntityCreationException;
import org.elis.event_booking.exceptions.EntityDeletionException;
import org.elis.event_booking.exceptions.EntityNotFoundException;
import org.elis.event_booking.service.definitions.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/places")
public class PlaceController {

    private final PlaceService placeService;

    @Autowired
    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @PostMapping
    public ResponseEntity<PlaceDTO> savePlace(@Valid @RequestBody PlaceDTO placeDTO) throws EntityCreationException {
        return new ResponseEntity<>(placeService.save(placeDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PlaceDTO>> getPlaces() {
        return new ResponseEntity<>(placeService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PlaceDTO> getPlaceById(@PathVariable long id) throws EntityNotFoundException {
        return new ResponseEntity<>(placeService.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/name")
    public ResponseEntity<List<PlaceDTO>> getPlaceByName(@RequestParam String name) {
        return new ResponseEntity<>(placeService.findByName(name), HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<Void> removePlace(@RequestParam long id) throws EntityNotFoundException, EntityDeletionException {
        placeService.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
