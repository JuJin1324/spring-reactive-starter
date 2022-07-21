package starter.reactive.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
    public Mono<Rendering> home(Authentication authentication) {
        return Mono.just(Rendering.view("home")
                .modelAttribute("items", itemService.getAllItems())
                .modelAttribute("cart", cartService.getCarts(cartName(authentication))
                        .defaultIfEmpty(new CartReadDto(cartName(authentication))))
                .modelAttribute("authentication", authentication)
                .build()
        );
    }

    private static String cartName(Authentication authentication) {
        return authentication.getName() + "'s Cart";
    }

    @PostMapping("/add/{itemId}")
    public Mono<String> addToCart(Authentication authentication,
                                  @PathVariable("itemId") String itemId) {
        return cartService.addItemToCart(cartName(authentication), itemId)
                .thenReturn("redirect:/");
    }

    @DeleteMapping("/delete/{itemId}")
    public Mono<String> deleteFromCart(Authentication authentication,
                                       @PathVariable("itemId") String itemId) {
        return cartService.deleteOneFromCart(cartName(authentication), itemId)
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
