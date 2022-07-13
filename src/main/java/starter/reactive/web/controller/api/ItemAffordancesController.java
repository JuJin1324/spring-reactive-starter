package starter.reactive.web.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import starter.reactive.domain.ecommerce.dto.ItemCreateDto;
import starter.reactive.domain.ecommerce.dto.ItemReadDto;
import starter.reactive.domain.ecommerce.dto.ItemUpdateDto;
import starter.reactive.domain.ecommerce.service.ItemService;

import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/13
 */

@RestController
@RequestMapping("/affordances/items")
@RequiredArgsConstructor
public class ItemAffordancesController {
    private final ItemService itemService;

    @GetMapping("")
    public Mono<CollectionModel<EntityModel<ItemReadDto>>> findAll() {
        ItemAffordancesController controller = methodOn(ItemAffordancesController.class);

        Mono<Link> aggregateRoot = linkTo(controller.findAll())
                .withSelfRel()
                .andAffordance(controller.addNewItem(null))
                .toMono();

        return itemService.getAllItems()
                .flatMap(readDto -> findOne(readDto.getItemId()))
                .collectList()
                .flatMap(models -> aggregateRoot
                        .map(selfLink -> CollectionModel.of(models, selfLink)));
    }

    @PostMapping("")
    public Mono<ResponseEntity<ItemReadDto>> addNewItem(@RequestBody Mono<EntityModel<ItemCreateDto>> model) {
        return model
                .map(EntityModel::getContent)
                .flatMap(itemCreateDto -> itemService.save(Mono.just(itemCreateDto)))
                .map(ItemReadDto::getItemId)
                .flatMap(this::findOne)
                .map(newModel -> ResponseEntity.created(newModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                        .body(newModel.getContent()));
    }

    @GetMapping("/{itemId}")
    public Mono<EntityModel<ItemReadDto>> findOne(@PathVariable("itemId") String itemId) {
        ItemAffordancesController controller = methodOn(ItemAffordancesController.class);

        Mono<Link> selfLink = linkTo(controller.findOne(itemId))
                .withSelfRel()
                .andAffordance(controller.updateItem(itemId, null))
                .toMono();

        Mono<Link> aggregateLink = linkTo(controller.findAll())
                .withRel(IanaLinkRelations.ITEM)
                .toMono();

        return Mono.zip(itemService.getOne(itemId), selfLink, aggregateLink)
                .map(o -> EntityModel.of(o.getT1(), Links.of(o.getT2(), o.getT3())));
    }

    @PutMapping("/{itemId}")
    public Mono<ResponseEntity<Void>> updateItem(@PathVariable("itemId") String itemId,
                                                 @RequestBody Mono<EntityModel<ItemUpdateDto>> item) {
        return item
                .map(EntityModel::getContent)
                .flatMap(content -> itemService.update(itemId, Mono.just(content)))
                .then(findOne(itemId))
                .map(model -> ResponseEntity.noContent()
                        .location(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).build());
    }
}
