package starter.reactive.domain.dining.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/02
 */

@Service
@RequiredArgsConstructor
public class KitchenService {
    private static final List<Dish> MENU   = Arrays.asList(
            new Dish("뿌링클 치킨"),
            new Dish("잔치 국수"),
            new Dish("탕수육")
    );
    private static final Random     PICKER = new Random();

    public Flux<Dish> getDishes() {
        return Flux.<Dish>generate(sink -> sink.next(randomDish()))
                .delayElements(Duration.ofMillis(250));
    }

    private Dish randomDish() {
        return MENU.get(PICKER.nextInt(MENU.size()));
    }


}
