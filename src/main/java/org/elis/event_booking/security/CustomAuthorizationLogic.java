package org.elis.event_booking.security;

import org.elis.event_booking.dto.UserDTO;
import org.elis.event_booking.model.Role;
import org.elis.event_booking.service.definitions.UserService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("customAuthorizationLogic")
public class CustomAuthorizationLogic {

    private final UserService userService;
    private final List<String> roles = List.of(
            Role.CUSTOMER.name(),
            Role.SELLER.name(),
            Role.ADMIN.name(),
            Role.SUPER_ADMIN.name()
    );

    public CustomAuthorizationLogic(UserService userService) {
        this.userService = userService;
    }

    public boolean decide(String authenticationEmail, long id) {
        // I expect the user with the email "authenticationEmail" to have a higher role than the user with the id "id"

        UserDTO authenticatedUser = userService.findByEmail(authenticationEmail);
        UserDTO userToActOn = userService.findById(id);

        if (authenticatedUser == null || userToActOn == null) return false;

        return roles.indexOf(authenticatedUser.getRole()) > roles.indexOf(userToActOn.getRole());
    }

}
