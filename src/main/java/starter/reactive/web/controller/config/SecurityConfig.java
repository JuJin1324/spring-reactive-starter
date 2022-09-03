package starter.reactive.web.controller.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.SecurityWebFilterChain;
import starter.reactive.domain.user.repository.UserRepository;

import java.util.Arrays;

/**
 * Created by Yoo Ju Jin(jujin1324@daum.net)
 * Created Date : 2022/07/21
 */

@Configuration
@EnableReactiveMethodSecurity
public class SecurityConfig {
    private static final String USER      = "USER";
    private static final String INVENTORY = "INVENTORY";

    @Bean
    ApplicationRunner userLoader(MongoOperations operations) {
        return args -> {
            operations.save(new starter.reactive.domain.user.entity.User(
                    "greg", "password", Arrays.asList(role(USER))));

            operations.save(new starter.reactive.domain.user.entity.User(
                    "manager", "password", Arrays.asList(role(USER), role(INVENTORY))));
        };
    }

//    @Bean
    public ReactiveUserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> userRepository.findByName(username)
                .map(user -> User.withDefaultPasswordEncoder()
                        .username(user.getName())
                        .password(user.getPassword())
                        .authorities(user.getRoles().toArray(new String[0]))
                        .build());
    }

//    @Bean
    public SecurityWebFilterChain myCustomSecurityPolicy(ServerHttpSecurity httpSecurity) {
        return httpSecurity
                .authorizeExchange(exchangeSpec -> exchangeSpec
                        .anyExchange().authenticated()
                        .and()
                        .httpBasic()
                        .and()
                        .formLogin())
                .csrf().disable()
                .build();
    }

    static String role(String auth) {
        return "ROLE_" + auth;
    }
}
