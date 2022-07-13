package starter.reactive.web.controller.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import starter.reactive.domain.ecommerce.dto.ItemReadDto;
import starter.reactive.domain.ecommerce.entity.Item;
import starter.reactive.domain.ecommerce.service.ItemService;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/13
 */

@WebFluxTest(controllers = ItemHypermediaController.class)
@AutoConfigureRestDocs
class ItemHypermediaControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ItemService itemService;

    @Test
    void findOneItem() {
        given(itemService.getOne("item-1"))
                .willReturn(Mono.just(new ItemReadDto(new Item("item-1", "Alf alarm clock", "nothing I really nned", 19.99))));

        /* TODO
         * WebTestClient 로 테스트 시 Controller 내의 HATEOAS 관련 로직인 linkTo 메서드가 blocking call 로 동작해서
         * BlockHound 가 오류를 던진다. 해당 테스트를 위해서는 build.gradle 에서 blockhound-junit-platform dependency 를 주석 후
         * gradle refresh 후에 테스트한다.
         */
        webTestClient.get().uri("/hypermedia/items/{itemId}", "item-1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("findOne-hypermedia",
                        preprocessResponse(prettyPrint()),
                        links(
                                linkWithRel("self").description("이 \'Item\'에 대한 공식 링크"),
                                linkWithRel("item").description("\'Item\' 목록 링크")
                        )
                ));
    }
}
