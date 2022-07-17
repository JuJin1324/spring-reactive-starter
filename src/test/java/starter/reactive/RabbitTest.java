package starter.reactive;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;
import starter.reactive.domain.ecommerce.dto.ItemCreateDto;
import starter.reactive.domain.ecommerce.service.ItemService;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/15
 */

@SpringBootTest // 1
@AutoConfigureWebTestClient // 2
@Testcontainers // 3
@ContextConfiguration   // 4
public class RabbitTest {

    @Container static RabbitMQContainer container =
            new RabbitMQContainer("rabbitmq:3.10.6-management-alpine"); // 5

    @Autowired
    WebTestClient webTestClient;    // 6

    @Autowired
    ItemService itemService;    // 7

    @DynamicPropertySource  // 8
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", container::getHost);
        registry.add("spring.rabbitmq.port", container::getAmqpPort);
    }

    @Test
    void verifyMessagingThroughAmqp() throws InterruptedException {
        webTestClient.post().uri("/amqp/items")
                .bodyValue(new ItemCreateDto("Alf alarm clock", "nothing important", 19.99))
                .exchange()
                .expectStatus().isCreated()
                .expectBody();

        Thread.sleep(1500L);

        webTestClient.post().uri("/amqp/items")
                .bodyValue(new ItemCreateDto("Smurf TV tray", "nothing important", 29.99))
                .exchange()
                .expectStatus().isCreated()
                .expectBody();

        Thread.sleep(2000L);

        itemService.getAllItems()
                .as(StepVerifier::create)
                .expectNextMatches(itemReadDto -> {
                    assertEquals("Alf alarm clock", itemReadDto.getName());
                    assertEquals("nothing important", itemReadDto.getDescription());
                    assertEquals(19.99, itemReadDto.getPrice());
                    return true;
                })
                .expectNextMatches(itemReadDto -> {
                    assertEquals("Smurf TV tray", itemReadDto.getName());
                    assertEquals("nothing important", itemReadDto.getDescription());
                    assertEquals(29.99, itemReadDto.getPrice());
                    return true;
                })
                .verifyComplete();
    }
}
