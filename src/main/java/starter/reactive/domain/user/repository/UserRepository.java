package starter.reactive.domain.user.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import starter.reactive.domain.user.entity.User;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/21
 */
public interface UserRepository extends ReactiveMongoRepository<User, String> {

    Mono<User> findByName(String name);
}
