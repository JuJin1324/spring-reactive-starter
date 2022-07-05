package starter.reactive.runner;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/05
 */

//@Component
public class ReactorDebuggingExampleRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Hooks.onOperatorDebug();

        Mono<Integer> source;
        if (true) {
            source = Flux.just(1, 2, 3, 4).elementAt(5);
        } else {
            source = Flux.range(1, 10).elementAt(5);
        }

        source
                .subscribeOn(Schedulers.parallel())
                .block();
    }
}
