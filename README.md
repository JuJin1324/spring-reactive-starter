# spring.reactive.starter

## Spring Webflux 실행 구조
> Spring Webflux 는 내부적으로 Reactor Netty 를 사용하고 Reactor Netty 는 실행 시 디폴트로 1개의 기본 스레드와 코어 갯수만큼의 이벤트 루프가 생성된다.  
> (4코어 8스레드인 경우 스레드를 따라서 8개의 이벤트루프가 생성된다.)
> 기존 Spring MVC 의 tomcat Thread 갯수는 100개가 디폴트이며 각 쓰레드는 Blocking 방식으로 실행된다.  
> 이벤트 루프는 비동기 Non-Blocking 으로 실행되기 때문에 블로킹 코드가 있으면 해당 블로킹 코드로 이벤트 루프 하나가 점유되어 버린다.  
> 즉 8개의 이벤트루프가 있는데 8개의 요청이 블로킹 코드 부분을 거치게 되면 8개의 이벤트루프가 모두 블로킹되어 버려서 MVC 보다 효율이 떨어지게 된다.    
> 참조사이트: [[번역] Concurrency in Spring WebFlux](https://timewizhan.tistory.com/entry/%EB%B2%88%EC%97%AD-Concurrency-in-Spring-WebFlux)  

## Spring Webflux 사용처
> Spring Webflux 는 IO 작업(Network, DB) 이 주인 애플리케이션에서 사용하며,   
> 연산작업(CPU 가 필요한)이 주인 애플리케이션에서는 효율이 좋지 않다.

## dependencies
### Spring Reactive Web
> 설명: 스프링 웹플러스 + 내장형 네티  
> build.gradle: `implementation 'org.springframework.boot:spring-boot-starter-webflux'`

### Thymeleaf
> 설명: 템플릿 엔진  
> build.gradle: `implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'`

### MongoDB Reactive
> 설명: Reactive stream driver 를 사용하는 몽고DB 라이브러리  
> build.gradle: `implementation 'org.springframework.boot:spring-boot-starter-data-mongodb-reactive'`  

## Run
### Gradle 
> 애플리케이션 빌드: `./gradlew build`  
> 애플리케이션 실행: `./gradlew bootRun`  


