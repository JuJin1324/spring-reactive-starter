package starter.reactive.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
    public Mono<Rendering> home(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
                                @AuthenticationPrincipal OAuth2User oAuth2User) {
        return Mono.just(Rendering.view("home")
                .modelAttribute("items", itemService.getAllItems())
                .modelAttribute("cart", cartService.getCarts(cartName(oAuth2User))
                        .defaultIfEmpty(new CartReadDto(cartName(oAuth2User))))
                .modelAttribute("userName", oAuth2User.getName())
                .modelAttribute("authorities", oAuth2User.getAuthorities())
                .modelAttribute("clientName", authorizedClient.getClientRegistration().getClientName())
                .modelAttribute("userAttributes", oAuth2User.getAttributes())
                .build()
        );
    }

    private static String cartName(OAuth2User oAuth2User) {
        return oAuth2User.getName() + "'s Cart";
    }

    @PostMapping("/add/{itemId}")
    public Mono<String> addToCart(@AuthenticationPrincipal OAuth2User oAuth2User,
                                  @PathVariable("itemId") String itemId) {
        return cartService.addItemToCart(cartName(oAuth2User), itemId)
                .thenReturn("redirect:/");
    }

    @DeleteMapping("/delete/{itemId}")
    public Mono<String> deleteFromCart(@AuthenticationPrincipal OAuth2User oAuth2User,
                                       @PathVariable("itemId") String itemId) {
        return cartService.deleteOneFromCart(cartName(oAuth2User), itemId)
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
