package starter.reactive.domain.ecommerce.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import starter.reactive.domain.ecommerce.entity.Cart;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/03
 */
public interface CartRepository extends ReactiveCrudRepository<Cart, String> {
}
