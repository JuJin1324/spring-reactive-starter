package starter.reactive.domain.ecommerce.service;

import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
public class BlockingCartService implements CartService {
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;

    public Mono<CartReadDto> getCarts(String cartId) {
        return cartRepository.findById(cartId)
                .map(CartReadDto::new);
    }

    public Mono<CartReadDto> addItemToCart(String cartId, String itemId) {
        Cart myCart = cartRepository.findById(cartId)
                .defaultIfEmpty(new Cart(cartId))
                .block();

        return myCart.getCartItems().stream()
                .filter(cartItem -> cartItem.getItem().getId().equals(itemId))
                .findAny()
                .map(cartItem -> {
                    cartItem.increment();
                    return Mono.just(myCart);
                })
                .orElseGet(() -> itemRepository.findById(itemId)
                        .map(CartItem::new)
                        .map(cartItem -> {
                            myCart.addCartItem(cartItem);
                            return myCart;
                        }))
                .flatMap(this.cartRepository::save)
                .map(CartReadDto::new);
    }

    @Override
    public Mono<CartReadDto> deleteOneFromCart(String cartName, String itemId) {
        return null;
    }
}
