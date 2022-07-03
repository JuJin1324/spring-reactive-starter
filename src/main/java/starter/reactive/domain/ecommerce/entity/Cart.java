package starter.reactive.domain.ecommerce.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/03
 */

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Cart {
    @Id
    private String id;

    private List<CartItem> cartItems;

    public Cart(String id, List<CartItem> cartItems) {
        this.id = id;
        this.cartItems = cartItems;
    }

    public Cart(String id) {
        this(id, new ArrayList<>());
    }

    public void addCartItem(CartItem cartItem) {
        this.cartItems.add(cartItem);
    }
}
