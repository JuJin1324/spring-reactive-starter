package starter.reactive.domain.ecommerce.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import starter.reactive.domain.ecommerce.dto.ItemReadDto;
import starter.reactive.domain.ecommerce.repository.ItemRepository;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/03
 */

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    public Flux<ItemReadDto> getAllItems() {
        return itemRepository.findAll()
                .map(ItemReadDto::new);
    }

}
