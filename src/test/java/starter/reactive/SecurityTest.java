package starter.reactive;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;
import starter.reactive.domain.ecommerce.repository.ItemRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/21
 */
@SpringBootTest
@AutoConfigureWebTestClient
public class SecurityTest {

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    ItemRepository itemRepository;

    @Test
    @WithMockUser(username = "alice", roles = {"SOME_OTHER_ROLE"})
    void addingInventoryWithoutProperRoleFails() {
        webTestClient.post().uri("/")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    @WithMockUser(username = "bob", roles = {"INVENTORY"})
    void addingInventoryWithProperSucceeds() {
        webTestClient.post().uri("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"name\": \"iPhone13 Pro\", \"description\": \"upgrade\", \"price\":999.99}")
                .exchange()
                .expectStatus().isCreated();

        itemRepository.findByNameLike("iPhone13 Pro")
                .as(StepVerifier::create)
                .expectNextMatches(item -> {
                    assertEquals("upgrade", item.getDescription());
                    assertEquals(999.99, item.getPrice());
                    return true;
                })
                .verifyComplete();
    }
}
