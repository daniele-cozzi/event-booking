package org.elis.event_booking.service.implementations;

import jakarta.transaction.Transactional;
import org.elis.event_booking.dto.CartDTO;
import org.elis.event_booking.dto.mapper.CartMapper;
import org.elis.event_booking.exceptions.EntityCreationException;
import org.elis.event_booking.exceptions.EntityDuplicateException;
import org.elis.event_booking.exceptions.EntityNotFoundException;
import org.elis.event_booking.model.Cart;
import org.elis.event_booking.model.Seat;
import org.elis.event_booking.model.User;
import org.elis.event_booking.repository.CartRepository;
import org.elis.event_booking.repository.SeatRepository;
import org.elis.event_booking.repository.UserRepository;
import org.elis.event_booking.service.definitions.CartService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final SeatRepository seatRepository;

    public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository, SeatRepository seatRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.seatRepository = seatRepository;
    }

    @Override
    public List<CartDTO> findAll() {
        List<Cart> carts = cartRepository.findAll();
        return CartMapper.INSTANCE.cartsToCartDTOs(carts);
    }

    @Override
    public CartDTO findById(long id) {
        Cart cart = cartRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("cart", "id", id));
        return CartMapper.INSTANCE.cartToCartDTO(cart);
    }

    @Override
    public CartDTO findByUserId(long id) {
        Cart cart = cartRepository.findByUserId(id).orElseThrow(() -> new EntityNotFoundException("cart", "user_id", id));
        return CartMapper.INSTANCE.cartToCartDTO(cart);
    }

    @Override
    public CartDTO addSeat(long user_id, long seat_id) {
        User user = userRepository.findById(user_id).orElseThrow(() -> new EntityNotFoundException("user", "id", user_id));
        Seat seat = seatRepository.findById(seat_id).orElseThrow(() -> new EntityNotFoundException("seat", "id", seat_id));

        Cart cart = user.getCart();

        if (cart.getSeats().contains(seat)) throw new EntityDuplicateException("cart", "seat_id", seat_id);

        cart.getSeats().add(seat);
        seat.getCarts().add(cart);

        try {
            cart = cartRepository.save(cart);
        } catch (Exception e) {
            throw new EntityCreationException("cart");
        }

        return CartMapper.INSTANCE.cartToCartDTO(cart);
    }

    @Override
    public boolean removeSeat(long user_id, long seat_id) {
        User user = userRepository.findById(user_id).orElseThrow(() -> new EntityNotFoundException("user", "id", user_id));
        Seat seat = seatRepository.findById(seat_id).orElseThrow(() -> new EntityNotFoundException("seat", "id", seat_id));

        Cart cart = user.getCart();

        if (!cart.getSeats().contains(seat)) throw new EntityNotFoundException("cart", "seat_id", seat_id);

        cart.getSeats().remove(seat);
        seat.getCarts().remove(cart);

        try {
            cartRepository.save(cart);
        } catch (Exception e) {
            throw new EntityCreationException("cart");
        }

        return true;
    }
}
