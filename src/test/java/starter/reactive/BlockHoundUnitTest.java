package starter.reactive;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import starter.reactive.domain.ecommerce.entity.Cart;
import starter.reactive.domain.ecommerce.repository.CartRepository;
import starter.reactive.domain.ecommerce.repository.ItemRepository;
import starter.reactive.domain.ecommerce.service.BlockingCartService;
import starter.reactive.domain.ecommerce.service.CartService;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/09
 */

@ExtendWith(MockitoExtension.class)
public class BlockHoundUnitTest {
    @Mock
    ItemRepository itemRepository;
    @Mock
    CartRepository cartRepository;

    CartService cartService;

    @BeforeEach
    void setUp() {
        cartService = new BlockingCartService(cartRepository, itemRepository);
    }

    //    @Test
    void threadSleepIsBlockingCall() {
        Mono.delay(Duration.ofSeconds(1))
                .flatMap(tick -> {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        return Mono.error(e);
                    }
                    return Mono.just(tick);
                })
                .as(StepVerifier::create)
                .verifyErrorMatches(throwable -> {
                    assertThat(throwable.getMessage())
                            .contains("Blocking call! java.lang.Thread.sleep");
                    return true;
                });
    }

    //    @Test
    void blockHoundShouldTrapBlockingCall() {
//        Item sampleItem = new Item("item1", "TV tray", "Alf TV tray", 19.99);
//        CartItem sampleCartItem = new CartItem(sampleItem);
//        Cart sampleCart = new Cart("My Cart", Collections.singletonList(sampleCartItem));

        /* stubbing */
        given(cartRepository.findById(anyString()))
                .willReturn(Mono.<Cart>empty().hide());
//        given(itemRepository.findById(anyString()))
//                .willReturn(Mono.just(sampleItem));
//        given(cartRepository.save(any()))
//                .willReturn(Mono.just(sampleCart));

        Mono.delay(Duration.ofSeconds(1))
                .flatMap(tick -> cartService.addItemToCart("My Cart", "item1"))
                .as(StepVerifier::create)
//                .verifyComplete();
                .verifyErrorSatisfies(throwable -> assertThat(throwable)
                        .hasMessageContaining("block()/blockFirst()/blockLast() are blocking"));
    }
}
