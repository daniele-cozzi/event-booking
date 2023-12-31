package org.elis.event_booking.controller;

import org.elis.event_booking.dto.CartDTO;
import org.elis.event_booking.exceptions.EntityCreationException;
import org.elis.event_booking.exceptions.EntityDuplicateException;
import org.elis.event_booking.exceptions.EntityNotFoundException;
import org.elis.event_booking.model.User;
import org.elis.event_booking.service.definitions.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/carts")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<List<CartDTO>> getCarts() {
        List<CartDTO> cartDTOs = cartService.findAll();
        return new ResponseEntity<>(cartDTOs, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CartDTO> getCartById(@PathVariable long id) throws EntityNotFoundException {
        return new ResponseEntity<>(cartService.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/user")
    public ResponseEntity<CartDTO> getCartsByUser(Authentication authentication) throws EntityNotFoundException {
        User user = (User) authentication.getPrincipal();
        CartDTO cartDTO = cartService.findByUserId(user.getId());
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @PostMapping(path = "/add-seat")
    public ResponseEntity<CartDTO> addSeat(Authentication authentication, @RequestParam("seat") long seat_id) throws EntityNotFoundException, EntityDuplicateException, EntityCreationException {
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(cartService.addSeat(user.getId(), seat_id), HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete-seat")
    public ResponseEntity<CartDTO> removeSeat(Authentication authentication, @RequestParam("seat") long seat_id) throws EntityNotFoundException, EntityCreationException {
        User user = (User) authentication.getPrincipal();
        cartService.removeSeat(user.getId(), seat_id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
