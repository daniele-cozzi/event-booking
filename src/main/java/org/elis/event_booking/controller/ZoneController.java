package org.elis.event_booking.controller;

import jakarta.validation.Valid;
import org.elis.event_booking.dto.ZoneDTO;
import org.elis.event_booking.exceptions.EntityCreationException;
import org.elis.event_booking.exceptions.EntityDeletionException;
import org.elis.event_booking.exceptions.EntityNotFoundException;
import org.elis.event_booking.service.definitions.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/zones")
public class ZoneController {

    private final ZoneService zoneService;

    @Autowired
    public ZoneController(ZoneService zoneService) {
        this.zoneService = zoneService;
    }

    @PostMapping
    public ResponseEntity<ZoneDTO> saveZone(@Valid @RequestBody ZoneDTO zoneDTO) throws EntityNotFoundException, EntityCreationException {
        return new ResponseEntity<>(zoneService.save(zoneDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ZoneDTO>> getZones() {
        return new ResponseEntity<>(zoneService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ZoneDTO> getZoneById(@PathVariable long id) throws EntityNotFoundException {
        return new ResponseEntity<>(zoneService.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/name")
    public ResponseEntity<List<ZoneDTO>> getZoneByName(@RequestParam String name) {
        return new ResponseEntity<>(zoneService.findByName(name), HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<Void> removeZone(@RequestParam long id) throws EntityNotFoundException, EntityDeletionException {
        zoneService.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
