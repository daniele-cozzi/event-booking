package org.elis.event_booking.service.definitions;

import org.elis.event_booking.dto.UserDTO;

import java.util.List;

public interface UserService {
    // UserDTO save(UserDTO userDTO);
    List<UserDTO> findAll();
    UserDTO findById(long id);
    UserDTO findByEmail(String email);
    UserDTO enableById(long id);
    UserDTO disableById(long id);
    boolean remove(long id);
}
