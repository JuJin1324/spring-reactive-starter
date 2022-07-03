package starter.reactive.domain.ecommerce.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import starter.reactive.domain.ecommerce.dto.CartReadDto;
import starter.reactive.domain.ecommerce.entity.Cart;
import starter.reactive.domain.ecommerce.entity.CartItem;
import starter.reactive.domain.ecommerce.repository.CartRepository;
import starter.reactive.domain.ecommerce.repository.ItemRepository;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/03
 */

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;

    public Mono<CartReadDto> getCarts(String cartId) {
        return cartRepository.findById(cartId)
                .map(CartReadDto::new);
    }

    public Mono<Void> addToCart(String cartId, String itemId) {
        return cartRepository.findById(cartId)
                .defaultIfEmpty(new Cart(cartId))
                .flatMap(cart -> cart.getCartItems().stream()
                        .filter(cartItem -> cartItem.getItem().getId().equals(itemId))
                        .findAny()
                        .map(cartItem -> {
                            cartItem.increment();
                            return Mono.just(cart);
                        })
                        .orElseGet(() -> itemRepository.findById(itemId)
                                .map(CartItem::new)
                                .map(cartItem -> {
                                    cart.addCartItem(cartItem);
                                    return cart;
                                })))
                .flatMap(this.cartRepository::save)
                .then();
    }
}
