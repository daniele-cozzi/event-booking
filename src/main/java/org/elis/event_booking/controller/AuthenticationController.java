package org.elis.event_booking.controller;

import jakarta.validation.Valid;
import org.elis.event_booking.dto.TokenDTO;
import org.elis.event_booking.dto.UserDTO;
import org.elis.event_booking.dto.UserLoginDTO;
import org.elis.event_booking.exceptions.*;
import org.elis.event_booking.security.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register-admin")
    public ResponseEntity<UserDTO> registerAdmin(@Valid @RequestBody UserDTO userDTO) throws EntityDuplicateException, EntityCreationException {
        return new ResponseEntity<>(authenticationService.registerAdmin(userDTO), HttpStatus.CREATED);
    }

    @PostMapping("/register-seller")
    public ResponseEntity<UserDTO> registerSeller(@Valid @RequestBody UserDTO userDTO) throws EntityDuplicateException, EntityCreationException {
        return new ResponseEntity<>(authenticationService.registerSeller(userDTO), HttpStatus.CREATED);
    }

    @PostMapping("/register-customer")
    public ResponseEntity<UserDTO> registerCustomer(@Valid @RequestBody UserDTO userDTO) throws EntityDuplicateException, EntityCreationException {
        return new ResponseEntity<>(authenticationService.registerCustomer(userDTO), HttpStatus.CREATED);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<TokenDTO> authenticateUser(@Valid @RequestBody UserLoginDTO userLoginDTO) throws EntityNotFoundException, UserNotActiveException, InvalidCredentialsException {
        return new ResponseEntity<>(authenticationService.authenticate(userLoginDTO), HttpStatus.OK);
    }

    /*
    TODO
     Introduce the JWT token refresh mechanism
      - When your JWT token expires or is close to expiring, instead of logging in again with your credentials,
        the user can use the refresh token (unique) to request a new JWT token.
      - You should keep track (on the database for example) of the tokens generated. Each token would have as attributes:
        - expirationDate
        - revokeDate (set to true when the refresh token endpoint is called)
      - Each user could be associated with a list of tokens if the project includes several front-ends
    */
}
