package org.elis.event_booking.security;

import jakarta.transaction.Transactional;
import org.elis.event_booking.dto.TokenDTO;
import org.elis.event_booking.dto.UserDTO;
import org.elis.event_booking.dto.UserLoginDTO;
import org.elis.event_booking.dto.mapper.UserMapper;
import org.elis.event_booking.exceptions.*;
import org.elis.event_booking.model.Cart;
import org.elis.event_booking.model.Role;
import org.elis.event_booking.model.User;
import org.elis.event_booking.repository.CartRepository;
import org.elis.event_booking.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Transactional(rollbackOn = Exception.class)
public class AuthenticationService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, CartRepository cartRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public void registerSuperAdmin(UserDTO userDTO) {
        // another super admin cannot be created -> I call the method only in the CommandLineRunner been
        userDTO.setRole(Role.SUPER_ADMIN.name());
        register(userDTO);
    }

    public UserDTO registerAdmin(UserDTO userDTO) {
        userDTO.setRole(Role.ADMIN.name());
        return register(userDTO);
    }

    public UserDTO registerSeller(UserDTO userDTO) {
        userDTO.setRole(Role.SELLER.name());
        return register(userDTO);
    }

    public UserDTO registerCustomer(UserDTO userDTO) {
        userDTO.setRole(Role.CUSTOMER.name());
        return register(userDTO);
    }

    private UserDTO register(UserDTO userDTO) {
        User user = UserMapper.INSTANCE.userDTOToUser(userDTO);

        // I encode the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // I create a cart for the user and I save it
        user.setCart(cartRepository.save(new Cart(0, user, new ArrayList<>())));

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new EntityDuplicateException("user", "email", user.getEmail());
        } catch (Exception e) {
            throw new EntityCreationException("user");
        }

        return UserMapper.INSTANCE.userToUserDTO(user);
    }

    public TokenDTO authenticate(UserLoginDTO userLoginDTO) {
        User user = userRepository.findByEmail(userLoginDTO.getEmail()).orElseThrow(() ->
                new EntityNotFoundException("user", "email", userLoginDTO.getEmail())
        );

        // I authenticate a user based on their email and password
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(), userLoginDTO.getPassword())
            );
        } catch (Exception e) {
            throw new InvalidCredentialsException();
        }

        if (!user.isActive()) {
            throw new UserNotActiveException(user.getEmail());
        }

        String token = jwtService.generateToken(user.getUsername());

        return new TokenDTO(token);
    }
}
