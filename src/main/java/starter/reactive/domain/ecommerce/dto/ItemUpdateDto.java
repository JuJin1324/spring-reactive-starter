package starter.reactive.domain.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/11
 */
@AllArgsConstructor
@Getter
public class ItemUpdateDto {
    private final String name;
    private final String description;
    private final double price;
}
