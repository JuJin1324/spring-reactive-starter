package starter.reactive.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import starter.reactive.dining.service.PoliteServer;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/02
 */

//@Component
@RequiredArgsConstructor
public class PoliteRestaurantRunner implements ApplicationRunner {
    private final PoliteServer server;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        server.doingMyJob()
                .subscribe(
                        dish -> System.out.println("Consuming " + dish),
                        System.err::println
                );
    }
}
