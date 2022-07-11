package starter.reactive.domain.ecommerce.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import starter.reactive.domain.ecommerce.dto.ItemCreateDto;
import starter.reactive.domain.ecommerce.dto.ItemReadDto;
import starter.reactive.domain.ecommerce.dto.ItemUpdateDto;
import starter.reactive.domain.ecommerce.entity.Item;
import starter.reactive.domain.ecommerce.repository.ItemRepository;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/03
 */

@Service
@RequiredArgsConstructor
public class ReactiveItemService implements ItemService {
    private final ItemRepository itemRepository;

    public Flux<ItemReadDto> getAllItems() {
        return itemRepository.findAll()
                .flatMap(item -> Mono.just(new ItemReadDto(item)));
    }

    public Flux<ItemReadDto> search(String name, String description, boolean useAnd) {
        Item item = new Item(name, description, 0.0);

        ExampleMatcher matcher = (useAnd ?
                ExampleMatcher.matchingAll() :
                ExampleMatcher.matchingAny())
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase()
                .withIgnorePaths("price");

        Example<Item> example = Example.of(item, matcher);
        return itemRepository.findAll(example)
                .flatMap(item1 -> Mono.just(new ItemReadDto(item1)));
    }

    @Override
    public Mono<ItemReadDto> getOne(String itemId) {
        return itemRepository.findById(itemId)
                .map(ItemReadDto::new);
    }

    @Override
    public Mono<ItemReadDto> save(Mono<ItemCreateDto> createDto) {
        return createDto
                .flatMap(dto -> itemRepository.save(new Item(dto.getName(), dto.getDescription(), dto.getPrice())))
                .map(ItemReadDto::new);
    }

    @Override
    public Mono<ItemReadDto> update(String itemId, Mono<ItemUpdateDto> updateDto) {
        return updateDto
                .flatMap(dto -> itemRepository.save(new Item(itemId, dto.getName(), dto.getDescription(), dto.getPrice())))
                .map(ItemReadDto::new);
    }
}
