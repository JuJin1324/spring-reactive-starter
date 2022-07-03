package starter.reactive.domain.dining.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/02
 */

@Service
@RequiredArgsConstructor
public class PoliteServer {
    private final KitchenService kitchenService;

    public Flux<Dish> doingMyJob() {
        return kitchenService.getDishes()
                .doOnNext(dish -> System.out.println("Thank you for " + dish + "!"))
                .doOnError(error -> System.out.println("So sorry about " + error.getMessage()))
                .doOnComplete(() -> System.out.println("Thanks for all your hard work!"))
                .map(Dish::deliver);
    }
}
