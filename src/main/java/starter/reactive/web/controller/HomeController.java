package starter.reactive.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import starter.reactive.domain.ecommerce.dto.CartReadDto;
import starter.reactive.domain.ecommerce.service.CartService;
import starter.reactive.domain.ecommerce.service.ItemService;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/02
 */

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ItemService itemService;
    private final CartService cartService;

    @GetMapping
    public Mono<Rendering> home() {
        return Mono.just(Rendering.view("home")
                .modelAttribute("items", itemService.getAllItems())
                .modelAttribute("cart", cartService.getCarts("My Cart")
                        .defaultIfEmpty(new CartReadDto("My Cart")))
                .build()
        );
    }

    @PostMapping("/add/{itemId}")
    public Mono<String> addToCart(@PathVariable("itemId") String itemId) {
        return cartService.addItemToCart("My Cart", itemId)
                .thenReturn("redirect:/");
    }

    @GetMapping("/search")
    Mono<Rendering> search(@RequestParam(required = false) String name,
                           @RequestParam(required = false) String description,
                           @RequestParam boolean useAnd) {
        return Mono.just(
                Rendering.view("home")
                        .modelAttribute("items", itemService.search(name, description, useAnd))
                        .modelAttribute("cart", cartService.getCarts("My Cart"))
                        .build()
        );

    }
}
