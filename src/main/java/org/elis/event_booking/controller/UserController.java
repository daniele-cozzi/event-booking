package org.elis.event_booking.controller;

import org.elis.event_booking.dto.UserDTO;
import org.elis.event_booking.exceptions.EntityDeletionException;
import org.elis.event_booking.exceptions.EntityNotFoundException;
import org.elis.event_booking.exceptions.UserEditActiveException;
import org.elis.event_booking.service.definitions.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable long id) throws EntityNotFoundException {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/email")
    public ResponseEntity<UserDTO> getUserByEmail(@RequestParam String email) throws EntityNotFoundException {
        return new ResponseEntity<>(userService.findByEmail(email), HttpStatus.OK);
    }

    // "authentication" is an object provided by Spring Security that represents the current user's authentication details
    @PatchMapping(path = "/enable")
    @PreAuthorize("@customAuthorizationLogic.decide(authentication.name, #id)")
    public ResponseEntity<UserDTO> enableUser(@RequestParam long id) throws EntityNotFoundException, UserEditActiveException {
        return new ResponseEntity<>(userService.enableById(id), HttpStatus.OK);
    }

    @PatchMapping(path = "/disable")
    @PreAuthorize("@customAuthorizationLogic.decide(authentication.name, #id)")
    public ResponseEntity<UserDTO> disableUser(@RequestParam long id) throws EntityNotFoundException, UserEditActiveException {
        return new ResponseEntity<>(userService.disableById(id), HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete")
    @PreAuthorize("@customAuthorizationLogic.decide(authentication.name, #id)")
    public ResponseEntity<Void> removeUser(@RequestParam long id) throws EntityNotFoundException, EntityDeletionException {
        userService.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}