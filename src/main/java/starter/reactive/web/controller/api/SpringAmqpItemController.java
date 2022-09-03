package starter.reactive.web.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/17
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/amqp/items")
public class SpringAmqpItemController {

//    private final AmqpTemplate amqpTemplate;
//
//    @PostMapping("")  // 1
//    Mono<ResponseEntity<Void>> addNewItemUsingSpringAmqp(@RequestBody Mono<ItemCreateDto> item) {   // 2
//        return item
//                .subscribeOn(Schedulers.boundedElastic())   // 3
//                .flatMap(createDto -> Mono
//                        .fromCallable(() -> {   // 4
//                            amqpTemplate.convertAndSend(    // 5
//                                    "hacking-spring-boot", "new-items-spring-amqp", createDto);
//                            return ResponseEntity.created(URI.create("/amqp/items")).build();    // 6
//                        }));
//    }
}
