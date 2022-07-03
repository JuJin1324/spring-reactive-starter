package starter.reactive.web.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import starter.reactive.domain.dining.service.Dish;
import starter.reactive.domain.dining.service.KitchenService;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/02
 */

@RestController
@RequiredArgsConstructor
public class ServerApiController {
    private final KitchenService kitchenService;

    /**
     * Httpie 를 통한 확인:
     * http --stream GET http://localhost:8080/server
     */
    @GetMapping(value = "/server", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Dish> serveDishes() {
        return kitchenService.getDishes();
    }

    /**
     * Httpie 를 통한 확인:
     * http --stream GET http://localhost:8080/served-dishes
     */
    @GetMapping(value = "/served-dishes", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Dish> deliverDishes() {
        return kitchenService.getDishes()
                .map(Dish::deliver);
    }
}
