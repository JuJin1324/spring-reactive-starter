package starter.reactive.domain.ecommerce.service;

import reactor.core.publisher.Mono;
import starter.reactive.domain.ecommerce.dto.CartReadDto;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/09
 */
public interface CartService {
    Mono<CartReadDto> getCarts(String cartId);

    Mono<CartReadDto> addItemToCart(String cartId, String itemId);

    Mono<CartReadDto> deleteOneFromCart(String cartName, String itemId);
}
