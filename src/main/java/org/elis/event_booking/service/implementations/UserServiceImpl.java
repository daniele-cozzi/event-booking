package org.elis.event_booking.service.implementations;

import jakarta.transaction.Transactional;
import org.elis.event_booking.dto.UserDTO;
import org.elis.event_booking.dto.mapper.UserMapper;
import org.elis.event_booking.exceptions.EntityDeletionException;
import org.elis.event_booking.exceptions.EntityNotFoundException;
import org.elis.event_booking.exceptions.UserEditActiveException;
import org.elis.event_booking.model.User;
import org.elis.event_booking.repository.UserRepository;
import org.elis.event_booking.service.definitions.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();
        return UserMapper.INSTANCE.usersToUserDTOs(users);
    }

    @Override
    public UserDTO findById(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("user", "id", id));
        return UserMapper.INSTANCE.userToUserDTO(user);
    }

    @Override
    public UserDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("user", "email", email));
        return UserMapper.INSTANCE.userToUserDTO(user);
    }

    @Override
    public UserDTO enableById(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("user", "id", id));

        user.setActive(true);

        try {
            user = userRepository.save(user);
            return UserMapper.INSTANCE.userToUserDTO(user);
        } catch (Exception e) {
            throw new UserEditActiveException("disable", "id", id);
        }
    }

    @Override
    public UserDTO disableById(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("user", "id", id));

        user.setActive(false);

        try {
            user = userRepository.save(user);
            return UserMapper.INSTANCE.userToUserDTO(user);
        } catch (Exception e) {
            throw new UserEditActiveException("enable", "id", id);
        }
    }

    @Override
    public boolean remove(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("user", "id", id));

        try {
            userRepository.delete(user);
        } catch (Exception e) {
            throw new EntityDeletionException("user", "id", id);
        }

        return true;
    }
}
