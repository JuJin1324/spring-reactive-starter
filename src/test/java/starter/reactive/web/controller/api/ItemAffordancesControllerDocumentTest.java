package starter.reactive.web.controller.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import starter.reactive.domain.ecommerce.dto.ItemCreateDto;
import starter.reactive.domain.ecommerce.dto.ItemReadDto;
import starter.reactive.domain.ecommerce.entity.Item;
import starter.reactive.domain.ecommerce.service.ItemService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/13
 */

@WebFluxTest(ItemAffordancesController.class)
@AutoConfigureRestDocs
class ItemAffordancesControllerDocumentTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ItemService itemService;

    @Test
    void findSingleItemAffordances() {
        given(itemService.getOne("item-1"))
                .willReturn(Mono.just(new ItemReadDto(new Item("item-1", "Alf alarm clock", "nothing I really need", 19.99))));

        webTestClient.get().uri("/affordances/items/{itemId}", "item-1")
                .accept(MediaTypes.HAL_FORMS_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("single-item-affordances", preprocessResponse(prettyPrint())));
    }

    @Test
    void findingAllItemsAffordances() {
        given(itemService.getAllItems())
                .willReturn(Flux.just(
                        new ItemReadDto(new Item("item-1", "Alf alarm clock", "nothing I really need", 19.99)),
                        new ItemReadDto(new Item("item-2", "TV tray", "I really need", 29.99))
                ));
        given(itemService.getOne("item-1"))
                .willReturn(Mono.just(new ItemReadDto(new Item("item-1", "Alf alarm clock", "nothing I really need", 19.99))));
        given(itemService.getOne("item-2"))
                .willReturn(Mono.just(new ItemReadDto(new Item("item-1", "TV tray", "I really need", 29.99))));

        webTestClient.get().uri("/affordances/items")
                .accept(MediaTypes.HAL_FORMS_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("findAll-affordances",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()))
                );
    }

    @Test
    void addNewItemAffordances() {
        ItemCreateDto createDto =
                new ItemCreateDto("Alf alarm clock", "nothing I really need", 19.99);
        given(itemService.save(any()))
                .willReturn(Mono.just(new ItemReadDto(new Item("item-1", "Alf alarm clock", "nothing I really need", 19.99))));

        given(itemService.getOne("item-1"))
                .willReturn(Mono.just(new ItemReadDto(new Item("item-1", "Alf alarm clock", "nothing I really need", 19.99))));
        given(itemService.getOne("item-2"))
                .willReturn(Mono.just(new ItemReadDto(new Item("item-1", "TV tray", "I really need", 29.99))));

        given(itemService.getAllItems())
                .willReturn(Flux.just(
                        new ItemReadDto(new Item("item-1", "Alf alarm clock", "nothing I really need", 19.99)),
                        new ItemReadDto(new Item("item-2", "TV tray", "I really need", 29.99))
                ));

        webTestClient.post().uri("/affordances/items")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_FORMS_JSON)
                .body(BodyInserters.fromValue(EntityModel.of(createDto)))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .consumeWith(document("addNewItem-affordances",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()))
                );
    }
}
