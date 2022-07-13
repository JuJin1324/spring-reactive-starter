package starter.reactive.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import starter.reactive.domain.ecommerce.dto.CartReadDto;
import starter.reactive.domain.ecommerce.dto.ItemReadDto;
import starter.reactive.domain.ecommerce.entity.Cart;
import starter.reactive.domain.ecommerce.entity.Item;
import starter.reactive.domain.ecommerce.service.ReactiveCartService;
import starter.reactive.domain.ecommerce.service.ReactiveItemService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/07
 */

@WebFluxTest(HomeController.class)
public class HomeControllerSliceTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ReactiveItemService itemService;
    @MockBean
    private ReactiveCartService cartService;

//    @Test
    void homePage() {
        given(itemService.getAllItems())
                .willReturn(Flux.just(
                        new ItemReadDto(new Item("id1", "name1", "desc1", 1.99)),
                        new ItemReadDto(new Item("id2", "name2", "desc2", 1.99))
                ));
        given(cartService.getCarts("My Cart"))
                .willReturn(Mono.just(new CartReadDto(new Cart("My Cart"))));

        webTestClient.get().uri("/").exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(stringEntityExchangeResult -> {
                    assertThat(stringEntityExchangeResult.getResponseBody())
                            .contains("action=\"/add/id1\"");
                    assertThat(stringEntityExchangeResult.getResponseBody())
                            .contains("action=\"/add/id2\"");
                });
    }
}
