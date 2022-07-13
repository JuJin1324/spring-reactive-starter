package starter.reactive.web.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.mediatype.alps.Alps;
import org.springframework.hateoas.mediatype.alps.Type;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import starter.reactive.domain.ecommerce.dto.ItemReadDto;
import starter.reactive.domain.ecommerce.service.ItemService;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mediatype.alps.Alps.descriptor;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;


/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/13
 */

@RestController
@RequestMapping("/hypermedia/items")
@RequiredArgsConstructor
public class ItemHypermediaController {
    private final ItemService itemService;

    @GetMapping("")
    public Flux<ItemReadDto> findAll() {
        return itemService.getAllItems();
    }

    @GetMapping("/{itemId}")
    public Mono<EntityModel<ItemReadDto>> findOne(@PathVariable("itemId") String itemId) {
        ItemHypermediaController controller = methodOn(ItemHypermediaController.class);

        Mono<Link> selfLink = linkTo(controller.findOne(itemId)).withSelfRel().toMono();
        Mono<Link> aggregateLink = linkTo(controller.findAll()).withRel(IanaLinkRelations.ITEM).toMono();

        return Mono.zip(itemService.getOne(itemId), selfLink, aggregateLink)
                .map(o -> EntityModel.of(o.getT1(), Links.of(o.getT2(), o.getT3())));
    }

    @GetMapping("/profile")
    public Alps profile() {
        return Alps.alps()
                .descriptor(Collections.singletonList(
                        descriptor()
                                .id(ItemReadDto.class.getSimpleName() + "-repr")
                                .descriptor(Arrays.stream(ItemReadDto.class.getDeclaredFields())
                                        .map(field -> descriptor().name(field.getName()).type(Type.SEMANTIC).build())
                                        .collect(Collectors.toList()))
                                .build())
                )
                .build();
    }
}
