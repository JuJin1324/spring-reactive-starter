package starter.reactive.domain.ecommerce.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import starter.reactive.domain.ecommerce.repository.ItemRepository;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/17
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class SpringAmqpItemService {
    private final ItemRepository itemRepository;

//    @RabbitListener(
//            ackMode = "MANUAL",
//            bindings = @QueueBinding(
//                    value = @Queue,
//                    exchange = @Exchange("hacking-spring-boot"),
//                    key = "new-items-spring-amqp"))
//    public Mono<Void> processNewItemViaSpringAmqp(ItemCreateDto createDto) {
//        log.debug("Consuming => {}", createDto);
//        return itemRepository.save(new Item(createDto.getName(), createDto.getDescription(), createDto.getPrice()))
//                .then();
//    }
}
