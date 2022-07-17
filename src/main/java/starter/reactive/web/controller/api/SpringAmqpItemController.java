package starter.reactive.web.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import starter.reactive.domain.ecommerce.dto.ItemCreateDto;

import javax.annotation.PostConstruct;
import java.net.URI;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/17
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/amqp/items")
public class SpringAmqpItemController {

    private final AmqpTemplate amqpTemplate;

    @PostMapping("")  // 1
    Mono<ResponseEntity<Void>> addNewItemUsingSpringAmqp(@RequestBody Mono<ItemCreateDto> item) {   // 2
        return item
                .subscribeOn(Schedulers.boundedElastic())   // 3
                .flatMap(createDto -> Mono
                        .fromCallable(() -> {   // 4
                            amqpTemplate.convertAndSend(    // 5
                                    "hacking-spring-boot", "new-items-spring-amqp", createDto);
                            return ResponseEntity.created(URI.create("/amqp/items")).build();    // 6
                        }));
    }
}
