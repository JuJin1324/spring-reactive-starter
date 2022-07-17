package starter.reactive.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;
import starter.reactive.domain.ecommerce.entity.Item;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/03
 */

//@Component
@RequiredArgsConstructor
public class TemplateDatabaseLoader implements ApplicationRunner {
    private final MongoOperations mongoOperations;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        mongoOperations.save(new Item("Alf alarm clock", "description1", 19.99));
        mongoOperations.save(new Item("Smurf TV tray", "description2", 24.99));
    }
}
