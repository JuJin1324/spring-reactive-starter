package starter.reactive.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/02
 */

@Controller
@RequiredArgsConstructor
public class HomeController {

    /**
     * 단순히 template 이름을 반환하는 Controller 의 경우
     * 굳이 Mono<String> 을 반환하지 않고 String 을 반환해도 된다.
     * 여기서 Mono<String> 반환한 것은 학습용이다.
     */
    @GetMapping
    Mono<String> home() {
        return Mono.just("home");
    }

}
