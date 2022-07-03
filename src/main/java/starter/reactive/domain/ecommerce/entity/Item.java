package starter.reactive.domain.ecommerce.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/03
 */

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Item {
    @Id
    private String id;

    private String name;

    private double price;

    public Item(String name, double price) {
        this.name = name;
        this.price = price;
    }
}
