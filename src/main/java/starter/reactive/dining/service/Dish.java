package starter.reactive.dining.service;

import lombok.Getter;
import lombok.ToString;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/02
 */

@Getter
@ToString(exclude = "isDelivered")
public class Dish {
    private final String name;

    private boolean isDelivered;

    public Dish(String name) {
        this.name = name;
        this.isDelivered = false;
    }

    Dish deliver() {
        this.isDelivered = true;
        return this;
    }
}
