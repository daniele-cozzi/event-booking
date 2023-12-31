package org.elis.event_booking.dto.mapper;

import org.elis.event_booking.dto.CartDTO;
import org.elis.event_booking.model.Cart;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CartMapper {
    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    @Mapping(source = "user.id", target = "user_id")
    CartDTO cartToCartDTO(Cart cart);

    List<CartDTO> cartsToCartDTOs(List<Cart> carts);

    @InheritInverseConfiguration
    Cart cartDTOToCart(CartDTO cartDTO);
}
