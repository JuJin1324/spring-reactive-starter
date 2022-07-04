package starter.reactive.domain.ecommerce.dto;

import lombok.Getter;
import starter.reactive.domain.ecommerce.entity.Item;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/03
 */

@Getter
public class ItemReadDto {
    private final String itemId;
    private final String name;
    private final String description;
    private final double price;

    public ItemReadDto(Item item) {
        this.itemId = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.price = item.getPrice();
    }
}
