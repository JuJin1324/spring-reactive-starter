package starter.reactive.domain.ecommerce.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/06
 */
class ItemTest {
    @Test
    void itemBasicsShouldWork() throws Exception {
        Item sampleItem = new Item("item1", "TV tray", 19.99);

        assertEquals("item1", sampleItem.getName());
        assertEquals("TV tray", sampleItem.getDescription());
        assertEquals(19.99, sampleItem.getPrice());

        Item sampleItem2 = new Item("item1", "TV tray", 19.99);
        assertEquals(sampleItem2, sampleItem);
    }
}
