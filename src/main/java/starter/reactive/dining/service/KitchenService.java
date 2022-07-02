package starter.reactive.dining.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/02
 */

@Service
@RequiredArgsConstructor
public class KitchenService {

    public Flux<Dish> getDishes() {
        return Flux.just(
                new Dish("뿌링클 치킨"),
                new Dish("잔치 국수"),
                new Dish("탕수육")
        );
    }
}
