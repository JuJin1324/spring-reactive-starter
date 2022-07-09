package starter.reactive.domain.ecommerce.service;

import reactor.core.publisher.Flux;
import starter.reactive.domain.ecommerce.dto.ItemReadDto;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/09
 */
public interface ItemService {
    Flux<ItemReadDto> getAllItems();

    Flux<ItemReadDto> search(String name, String description, boolean useAnd);
}
