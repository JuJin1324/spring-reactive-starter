package starter.reactive.web.controller.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/11
 */

@WebFluxTest(controllers = ItemApiController.class)
@AutoConfigureRestDocs
class ItemApiControllerDocumentationTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ItemService itemService;

    @Test
    void findingAllItems() {
        given(itemService.getAllItems())
                .willReturn(Flux.just(
                        new ItemReadDto(new Item("item-1", "Alf alarm clock", "nothing I really need", 19.99)),
                        new ItemReadDto(new Item("item-2", "TV tray", "I really need", 29.99))
                ));

        webTestClient.get().uri("/api/items")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("findAll",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].itemId").description("아이템 ID"),
                                fieldWithPath("[].name").description("아이템 이름"),
                                fieldWithPath("[].description").description("아이템 설명"),
                                fieldWithPath("[].price").description("아이템 가격")
                        ))
                );
    }

    @Test
    void addNewItem() {
        ItemCreateDto createDto =
                new ItemCreateDto("Alf alarm clock", "nothing I really need", 19.99);
        given(itemService.save(any()))
                .willReturn(Mono.just(new ItemReadDto(new Item("item-1", "Alf alarm clock", "nothing I really need", 19.99))));

        webTestClient.post().uri("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(createDto))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .consumeWith(document("addNewItem",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("아이템 이름"),
                                fieldWithPath("description").description("아이템 설명"),
                                fieldWithPath("price").description("아이템 가격")
                        ))
                );
    }
}
