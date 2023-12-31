package org.elis.event_booking.dto.mapper;

import org.elis.event_booking.dto.UserDTO;
import org.elis.event_booking.model.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "cart.id", target = "cart_id")
    UserDTO userToUserDTO(User user);

    List<UserDTO> usersToUserDTOs(List<User> users);

    @InheritInverseConfiguration
    User userDTOToUser(UserDTO userDTO);
}
