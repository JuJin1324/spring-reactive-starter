package starter.reactive.domain.ecommerce.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import starter.reactive.domain.ecommerce.dto.ItemCreateDto;
import starter.reactive.domain.ecommerce.dto.ItemReadDto;
import starter.reactive.domain.ecommerce.dto.ItemUpdateDto;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/09
 */
public interface ItemService {
    Flux<ItemReadDto> getAllItems();

    Flux<ItemReadDto> search(String name, String description, boolean useAnd);

    Mono<ItemReadDto> getOne(String itemId);

    Mono<ItemReadDto> save(Mono<ItemCreateDto> createDto);

    Mono<ItemReadDto> update(String itemId, Mono<ItemUpdateDto> updateDto);
}
