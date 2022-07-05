package starter.reactive.domain.ecommerce.dto;

import lombok.Getter;
import lombok.ToString;
import starter.reactive.domain.ecommerce.entity.CartItem;
import starter.reactive.domain.ecommerce.entity.Item;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/03
 */

@Getter
@ToString
public class CartItemReadDto {
    private final String itemId;
    private final String name;
    private final double price;
    private final int    quantity;

    public CartItemReadDto(CartItem cartItem) {
        Item item = cartItem.getItem();
        this.itemId = item.getId();
        this.name = item.getName();
        this.price = item.getPrice();

        this.quantity = cartItem.getQuantity();
    }
}
