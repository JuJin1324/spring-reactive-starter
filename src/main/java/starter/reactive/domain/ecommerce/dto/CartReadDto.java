package starter.reactive.domain.ecommerce.dto;

import lombok.Getter;
import lombok.ToString;
import starter.reactive.domain.ecommerce.entity.Cart;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/03
 */

@Getter
@ToString
public class CartReadDto {
    private final String                cartId;
    private       List<CartItemReadDto> cartItems;

    public CartReadDto(Cart cart) {
        this.cartId = cart.getId();
        this.cartItems = cart.getCartItems().stream()
                .map(CartItemReadDto::new)
                .collect(Collectors.toList());
    }

    public CartReadDto(String cartId) {
        this.cartId = cartId;
    }
}
