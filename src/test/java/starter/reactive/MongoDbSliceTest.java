package starter.reactive;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import starter.reactive.domain.ecommerce.entity.Item;
import starter.reactive.domain.ecommerce.repository.ItemRepository;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/07
 */

@DataMongoTest
public class MongoDbSliceTest {
    @Autowired
    private ItemRepository itemRepository;

    @Test
    void itemRepositorySavesItems() {
        Item sampleItem = new Item("name", "description", 1.99);

        itemRepository.save(sampleItem)
                .as(StepVerifier::create)
                .expectNextMatches(item -> {
                    assertNotNull(item.getId());
                    assertEquals("name", item.getName());
                    assertEquals("description", item.getDescription());
                    assertEquals(1.99, item.getPrice());

                    return true;
                })
                .verifyComplete();
    }
}
