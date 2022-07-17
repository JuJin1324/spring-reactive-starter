package starter.reactive.domain.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/11
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class ItemCreateDto {
    private String name;
    private String description;
    private double price;
}
