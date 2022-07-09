package starter.reactive.domain.ecommerce.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import starter.reactive.domain.ecommerce.dto.CartItemReadDto;
import starter.reactive.domain.ecommerce.entity.Cart;
import starter.reactive.domain.ecommerce.entity.CartItem;
import starter.reactive.domain.ecommerce.entity.Item;
import starter.reactive.domain.ecommerce.repository.CartRepository;
import starter.reactive.domain.ecommerce.repository.ItemRepository;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/06
 */

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CartRepository cartRepository;

    private ReactiveCartService cartService;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 정의
        Item item = new Item("item1", "TV tray", "Alf TV tary", 19.99);
        CartItem cartItem = new CartItem(item);
        Cart cart = new Cart("My Cart", Collections.singletonList(cartItem));

        // 스텁 정의
        given(cartRepository.findById(anyString())).willReturn(Mono.empty());
        given(itemRepository.findById(anyString())).willReturn(Mono.just(item));
        given(cartRepository.save(any())).willReturn(Mono.just(cart));

        cartService = new ReactiveCartService(cartRepository, itemRepository);
    }

    @Test
    void addItemToEmptyCartShouldProduceOneCartItem() throws Exception {
        cartService.addItemToCart("My Cart", "item1")
                .as(StepVerifier::create)
                .expectNextMatches(cart -> {
                    assertThat(cart.getCartItems()).extracting(CartItemReadDto::getQuantity)
                            .containsExactlyInAnyOrder(1);
                    assertThat(cart.getCartItems()).extracting(CartItemReadDto::getItemId)
                            .containsExactlyInAnyOrder("item1");
                    assertThat(cart.getCartItems()).extracting(CartItemReadDto::getName)
                            .containsExactlyInAnyOrder("TV tray");
                    assertThat(cart.getCartItems()).extracting(CartItemReadDto::getPrice)
                            .containsExactlyInAnyOrder(19.99);
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void alternativeWayToTest() {
        StepVerifier.create(cartService.addItemToCart("My Cart", "item1"))
                .expectNextMatches(cart -> {
                    assertThat(cart.getCartItems()).extracting(CartItemReadDto::getQuantity)
                            .containsExactlyInAnyOrder(1);
                    assertThat(cart.getCartItems()).extracting(CartItemReadDto::getItemId)
                            .containsExactlyInAnyOrder("item1");
                    assertThat(cart.getCartItems()).extracting(CartItemReadDto::getName)
                            .containsExactlyInAnyOrder("TV tray");
                    assertThat(cart.getCartItems()).extracting(CartItemReadDto::getPrice)
                            .containsExactlyInAnyOrder(19.99);
                    return true;
                })
                .verifyComplete();
    }
}
