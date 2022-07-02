package starter.reactive.dining.service;

import lombok.Getter;
import lombok.ToString;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/02
 */

@Getter
@ToString(exclude = "delivered")
public class Dish {
    private final String description;

    private boolean delivered;

    public Dish(String description) {
        this.description = description;
        this.delivered = false;
    }

    public static Dish deliver(Dish dish) {
        Dish deliveredDish = new Dish(dish.description);
        deliveredDish.delivered = true;
        return deliveredDish;
    }
}
