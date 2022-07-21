# spring.reactive.starter
## JDK
> version: amazon corretto 11

## Spring Webflux 실행 구조
> Spring Webflux 는 내부적으로 Reactor Netty 를 사용하고 Reactor Netty 는 실행 시 디폴트로 
> 1개의 기본 스레드(Client와의 Connection 수락 역할, 아마 이벤트 루프이지 않을까 생각)와     
> 코어 갯수 * 2만큼의 Worker Thread 가 생성된다.    
> 기존 Spring MVC 의 tomcat Thread 갯수는 100개가 디폴트이며 각 쓰레드는 Blocking 방식으로 실행된다.  
> Worker Thread 는 비동기 Non-Blocking 으로 실행되기 때문에 블로킹 코드가 있으면 해당 블로킹 코드로 Worker Thread 하나가 점유되어 버린다.  
> 즉 8개의 Worker Thread가 있는데 8개의 요청이 블로킹 코드 부분을 거치게 되면 8개의 Worker Thread가 모두 블로킹되어 버려서 MVC 보다 효율이 떨어지게 된다.    
> 
> 참조사이트   
> [[번역] Concurrency in Spring WebFlux](https://timewizhan.tistory.com/entry/%EB%B2%88%EC%97%AD-Concurrency-in-Spring-WebFlux)  
> [Spring WebFlux는 어떻게 적은 리소스로 많은 트래픽을 감당할까?](https://devahea.github.io/2019/04/21/Spring-WebFlux%EB%8A%94-%EC%96%B4%EB%96%BB%EA%B2%8C-%EC%A0%81%EC%9D%80-%EB%A6%AC%EC%86%8C%EC%8A%A4%EB%A1%9C-%EB%A7%8E%EC%9D%80-%ED%8A%B8%EB%9E%98%ED%94%BD%EC%9D%84-%EA%B0%90%EB%8B%B9%ED%95%A0%EA%B9%8C/)  

## Spring Webflux 사용처
> Spring Webflux 는 IO 작업(Network, DB) 이 주인 애플리케이션에서 Non-Blocking 방식으로 사용하며,   
> 연산작업(CPU 사용이 많은)이 많거나 Blocking IO 를 사용하는 애플리케이션에서는 성능 저하가 일어난다.  
> 
> 참조사이트   
> [내가 만든 WebFlux가 느렸던 이유.pdf](https://rlxuc0ppd.toastcdn.net/presentation/%5BNHN%20FORWARD%202020%5D%EB%82%B4%EA%B0%80%20%EB%A7%8C%EB%93%A0%20WebFlux%EA%B0%80%20%EB%8A%90%EB%A0%B8%EB%8D%98%20%EC%9D%B4%EC%9C%A0.pdf)

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

### Actuator
> 설명:   
> build.gradle: `implementation 'org.springframework.boot:spring-boot-starter-actuator'`

## Run
### 빌드 및 실행
> Gradle
> 애플리케이션 빌드: `./gradlew build`  
> 애플리케이션 실행: `./gradlew bootRun`  
>
> Maven
> 애플리케이션 빌드: `./mvnw build`  
> 애플리케이션 실행: `./mvnw spring-boot:run` 

## Debug
### Hooks
> 자바의 스택 트레이스는 동일한 스레드 내에서만 이어지며, 스레드 경계를 넘어서지 못한다.  
> 리액터 프로젝트에서 리액트 플로우 연산(map().flatmap().map() 과 같은 이어진 연산)이 모두 동일한 스레드에서 수행된다는 보장이 없다.  
> 리액터 프로젝트에서 자바의 스택 트레이스를 사용하게 되면 스레드 경계를 넘어선 StackTrace 에러 메시지를 표시할 수 없다.  
> 
> build.gradle 에 추가: `implementation 'io.projectreactor.tools:blockhound:1.0.6.RELEASE'`
> 리액터 연산 시작 전에 맨 위에 `Hooks.onOperatorDebug();` 명령어를 추가하면 명령어 이후의 연산에서 스레드를 넘어선 스택 트레이스가 표시된다.  
> 
> 주의) 리액터가 스레드별 스택 세부정보를 스레드 경계를 넘어서 전달하는 과정에는 굉장히 많은 비용이 든다. 이런 비용 이슈 때문에 자바에서 스레드 경계를 넘어 정보를
> 전달하는 것을 기본적으로 허용하지 않는 이유일 것이다. 따라서 성능 문제를 일으킬 수 있으므로 실제 운영환경 또는 실제 벤치마크에서는 `Hooks.onOperatorDebug();`를 
> 호출해서는 안된다.

## Test
### BlockHound JUnit
> BlockHound Hook 을 JUnit Test 에서 사용하기 위해서 build.gradle 에 다음을 추가한다.
> ```groovy
> testImplementation 'io.projectreactor.tools:blockhound-junit-platform:1.0.6.RELEASE'
> testRuntimeOnly(
>         'org.junit.jupiter:junit-jupiter-engine',
>         'org.junit.vintage:junit-vintage-engine',
>         'org.junit.platform:junit-platform-launcher',
>         'org.junit.platform:junit-platform-runner'
> )
> ```

## Deploy
### 실행가능한 Jar 파일 생성
> Gradle 
> 현재 프로젝트의 경로 이동 후 `./gradlew clean bootJar` 를 실행한다.  
> 이후 현재 프로젝트의 경로 기준 build/libs 디렉터리 아래 생성된 `.jar` 파일로 배포한다.   
>
> Maven  
> 현재 프로젝트의 경로 이동 후 `./mvnw clean package` 를 실행한다.
> 이후 현재 프로젝트의 경로 기준 target 디렉터리 아래 생성된 `.jar` 파일로 배포한다.

### Docker
> 현재 프로젝트의 docker/Dokerfile 을 참조한다.   
> Docker 는 캐시 시스템을 가지고 있어서 컨테이너 빌드에 소요되는 시간을 줄이기 위해 계층화를 이용한다.
> 애플리케이션 역시 계층화하여 Docker 의 캐시 시스템을 적극 활용한다. 
> 참고로 Dockerfile 을 테스트 시에 주의 점이 있다. 현재 프로젝트에서 Dockerfile 이 docker 디렉터리 안에 존재한다.
> Dockerfile 은 Dockerfile 이 존재하는 디렉터리 바깥의 파일은 COPY 명령어로 접근할 수 없다. 그래서 Dockerfile 로 
> target/*.jar 혹은 build/libs/*.jar 를 실습하기 위해서는 해당 .jar 파일을 docker 디렉터리 내부로 가져온 후에 
> `ARG JAR_FILE=*.jar` 로 수정하여 실습한다.

### 애플리케이션 배포 파일 계층화
> Gradle  
> ```groovy
> bootJar {
>     layered
> }
> ```
> Maven  
> ```xml
> <plugin>
>   <groupId>org.springframework.boot</groupId>
>   <artifactId>spring-boot-maven-plugin</artifactId>
>   <configuration>
>       <layers>
>           <enabled>true</enabled>
>       </layers>
>   </configuration>
> </plugin>
> ```

### SpringBoot 에서 Docker 이미지 빌드
> 스프링 부트는 Dockerfile 없이도 도커 컨테이너 이미지를 빌드할 수 있는 기능을 제공한다.  
> Gradle: `./gradlew bootBuildImage`
> 
> Maven: `./mvnw spring-boot:build-image`

## Actuator
### dependency
> build.gradle 에 다음 추가 `implementation 'org.springframework.boot:spring-boot-starter-actuator'`

### health
> 설명: 애플리케이션 헬스 체크  
> URL: `GET localhost:8080/actuator/health`   
> 디폴트 리턴 정보: `{"status":"UP"}`  
> 
> 추가 정보 표시 설정   
> application.yml
> ```yaml
> management:
>     endpoint:
>         health:
>             show-details: always
> ```
> 

### info
> 설명: 사용자 지정 커스텀 정보(애플리케이션 버전 정보 및 git 버전 정보 등) 표시      
> URL: `GET localhost:8080/actuator/info`  
> 주의: SpringBoot 2.5.0 이후 버전부터는 application.yml 에 info endpoint 사용 설정해야함.  
> 
> application.yml
> ```yaml
> management:
>     endpoints:
>         web:
>             exposure:
>                 include: health,info
> ```
> 
> 사용자 커스텀 정보 지정  
> application.yml  
> ```yaml
> management:
>     info:
>         env:
>             enabled: true
> ```
> 
> Gradle  
> build.gradle
> ```groovy
> ...
> dependencies {
>     ...
> }
> 
> processResources {
>    filesMatching("**/application.yml") {
>        expand(project.properties)
>    }
> }
> ```
> 
> application.yml
> ```yaml
> info:
>     project.version: '${version}'
>     java.version: '${sourceCompatibility}'
> ```
> 
> Maven  
> ```yaml
> info:
>     project.version: '@project.version@'
>     java.version: '@java.version@' 
> ```

### info - git 버전 정보 표시
> Gradle  
> build.gradle
> ```groovy
> plugins {
>     ...
>     id "com.gorylenko.gradle-git-properties" version '2.2.3'
> }
> ```
>
> Maven
> pom.xml
> ```xml
> <plugin>
>   <groupId>pl.project13.maven</groupId>
>   <artifactId>git-commit-id-plugin</artifactId>
> </plugin>
> ```

## Reactor map vs flatMap
### map
> Mono / Flux 에서 map 을 사용하게 되면 동기(synchronous)로 처리한다.   
> `Mono<T> -> Mono<T>` 의 경우 비동기로 처리하는 의미가 없기 때문에 굳이 flatMap 을 사용하지 않고 map 을 사용한다.

### flatMap
> Mono / Flux 에서 flatMap 을 사용하게 되면 비동기(asynchronous)로 처리한다.  
> `Flux<T> -> Flux<T>`, `Flux<T> -> Flux<U>` 의 경우 성능 상 이점이 많기 때문에 map 을 쓰지 않고 flatMap 을 사용한다. 
> `Mono<T> -> Flux<U>` 의 경우 비동기로 병렬 동작하는 publisher를 사용하는 경우 동기 실행보다 빠른 처리가 가능해진다. 
> 
> `Mono<T> -> Mono<U>`의 flatMap의 경우 성능 상 이점이 아닌 Publisher 객체 타입 변환이 목적이기 때문에 flatMap을 사용한다.
> 성능 상 이점이 없음으로 `Mono<T> -> Mono<U>` 의 경우 map 을 사용해도 된다.
> 
> 정리하면 Mono -> Mono 의 경우 map 을 사용해도 무관하며, Flux -> Flux 의 경우 필히 flatMap 을 사용한다.

### 참조사이트
> [Reactor map, flatMap method는 언제 써야할까?](https://luvstudy.tistory.com/95)  

## Reactor publishOn vs subscribeOn
### Scheduler
> publishOn 혹은 subscribeOn 에서 스케줄러를 등록하여 블로킹 코드를 별도의 스레드에 할당해서 스레드 낭비를 방지할 수 있다.  
> Scheduler 는 다음과 같다.  
> Schedulers.immediate(): 현재 스레드  
> Schedulers.single(): 재사용 가능한 하나의 스레드. 현재 수행 중인 리액터 플로우뿐만 아니라 호출되는 모든 작업이 동일한 하나의 스레드에서 실행된다.  
> Schedulers.newSingle(): 새로 생성한 전용 스레드  
> Schedulers.boundedElastic(): 작업량에 따라 스레드 숫자가 늘어나거나 줄어드는 신축성있는 스레드 풀  
> Schedulers.parallel(): 병렬 작업에 적합하도록 최적화된 고정 크기 워커 스레드 풀  
> Schedulers.fromExecutorService(): ExecutorService 인스턴스를 감싸서 재사용  
> 
> single(), newSingle, parallel() 은 논블로킹 작업에 사용되는 스레드를 생성한다. 이 세가지 스케줄러에 의해 생성되는 스레드는 리액터의 NonBlocking 
> 인터페이스를 구현한다. 따라서 block(), blockFirst(), blockLast() 같은 블로킹 코드가 사용되면 IllegalStateException 이 발생한다.

### publishOn
> 호출되는 시점 이후로는 지정한 스케줄러를 사용한다. 이 방법을 사용하면 사용하는 스케줄러를 여러 번 바꿀 수도 있다.    

### subscribeOn
> 플로우 전 단계에 걸쳐 사용되는 스케줄러를 지정한다. 플로우 전체에 영향을 미치므로 `publishOn` 에 비해 영향 범위가 더 넓다.  
> 리액터 플로우에서 `subscribeOn` 이 어디에 위치하든 해당 플로우 전체가 `subscribOn` 으로 지정한 스레드에서 실행된다. 다만 나중에 `publishOn` 으로
> 스레드를 다시 지정하면, 지정한 지점 이후부터는 `publishOn` 으로 새로 지정한 스레드에서 리액터 플로우가 실행된다.

## just vs defer vs fromCallable
### just
> 구독 이전에 just 선언 만으로 just 안의 내용을 인스턴스화 한다.  
> ex) `Mono<Item> just = Mono.just(ItemService.getOne("item-1"));` 선언 시에 ItemService.getOne 메서드가 실행된다.  
> 마치 Optional.orElse() 과 비슷하다.(eager)

### defer
> 구독이 발생해야 안의 내용을 실행하여 인스턴스화 한다.
> ex) `Mono<Item> defer = Mono.defer(() -> Mono.just(ItemService.getOne("item-1"));` 선언이 되어도 defer 안의 내용은 실행되지 않고 
> 구독이 발생해야 안의 내용이 실행된다.  
> 마치 Optional.orElseGet() 과 비슷하다.(lazy)

### fromCallable
> defer 와 유사하다. defer 의 경우 내부 람다 함수에서 Mono 를 반환해야하지만 callable은 Mono 가 아닌 인스턴스를 반환할 수 있다.    
> ex) `Mono<Item> callable = Mono.fromCallable(() -> ItemService.getOne("item-1"));`  
> defer 와 마찬가지로 구독이 발생해야 안의 내용이 실행된다.(lazy)

### boundedElastic + fromCallable
> Schedulers.boundedElastic() + fromCallable() 을 사용하여 fromCallable 안에 블로킹 코드를 넣어두는 방식으로
> 블로킹 코드를 논블로킹하게 구현할 수 있다.  
> ex)
> ```java
> Mono
>   .subscribeOn(Schedulers.boundedElastic())
>   .fromCallable(() -> {
>       return ...
>   });
> ```

## doOnNext vs map
### doOnNext
> 한 개의 시퀀스가 전달 될 때마다 doOnNext 이벤트 발생, map 이 아님으로 return 하지 않으며 return 하지 않더라도 매개변수로 들어온 값을 그대로
> 방출한다.

### map
> 매개변수를 다른 타입으로 변환하여 방출한다. 방출할 타입의 변수를 return 한다.

### 참조사이트
> [Reactor just, defer, fromCallable 에 대하여](https://binux.tistory.com/135?category=907689)

## Spring REST Doc
### Asciidoctor
> API 문서화 도구

### Asciidoctor - Gradle
> snippet 인 adoc 파일 생성을 위해서 다음을 추가한다.
> build.gradle
> ```groovy
> plugins {
>   ...
>   id "org.asciidoctor.jvm.convert" version '3.3.2'
> }
> 
> dependencies {
>   ...
>   // WebTestClient 로 테스트 하는 경우는 다음을 추가한다.
>   testImplementation 'org.springframework.restdocs:spring-restdocs-webtestclient'
>   // MockMvc 로 테스트 하는 경우는 다음을 추가한다.
>   testImplementation 'org.springframework.restdocs:spring-restdocs-mockmvc' 
> }
> 
> tasks.named('test') {
>     outputs.dir snippetsDir
>     useJUnitPlatform()
> }
> 
> ext {
>   snippetsDir = file('build/generated-snippets')
> }
> 
> asciidoctor {
>   inputs.dir snippetsDir
>   dependsOn test
> }
> asciidoctor.doFirst {
>     delete file('src/main/resources/static/docs')
> }
> ```
> 
> snippet 파일인 .adoc 파일을 통해서 전체 API HTML 문서를 생성하기 위해서는 다음을 추가한다.  
> build.gradle
> ```groovy
> bootJar {
>   dependsOn asciidoctor
>   copy {
>       from '${asciidoctor.outputDir}'
>       into 'BOOT-INF/classes/statis/docs'
>   }
>   finalizedBy 'copyDocument'
> }
> 
> task copyDocument(type: Copy) {
>     dependsOn bootJar
>     from file("src/docs/asciidoc")
>     into file("src/main/resources/static/docs")
> }
> ```
> 
> src 아래에 docs/asciidoc 디렉터리를 생성 후 API HTML 을 생성하도록 템플릿을 작성한다.
> src/docs/asciidoce/api.adoc
> ```
> ifndef::snippets[]
> :snippets: ./build/generated-snippets
> endif::[]
> 
> == Item
> === 전체 조회
> ==== Request
> include::{snippets}/findAll/http-request.adoc[]
> ==== Response
> include::{snippets}/findAll/http-response.adoc[]
> ==== Fields
> include::{snippets}/findAll/response-fields.adoc[]
> ```
> 
> 참조사이트: [Spring REST Docs 적용 (Gradle 7)](https://xlffm3.github.io/spring%20&%20spring%20boot/rest-docs/)

### Asciidoctor - Maven
> 참조사이트: [Maven + MockMvc 환경에서 Spring Rest Docs 써보기](https://berrrrr.github.io/programming/2021/01/24/how-to-use-spring-rest-docs/)  

## RabbitMQ 메시징 테스트
### TestContainers
> RabbitMQ 를 직접 설치하지 않고 도커 컨테이너를 이용해서 테스트 용도로 설치했다가 테스트가 완료되면 자동으로 제거될 수 있도록 해주는 라이브러리  
>
> Gradle  
> build.gradle
> ```groovy
> dependencies {
>   implementation 'org.springframework.boot:spring-boot-starter-amqp'  // SpringBoot RabbitMQ 라이브러리 
>   ...
>   implementation platform('org.testcontainers:testcontainers-bom:1.17.3') //import bom
>   testImplementation 'org.testcontainers:junit-jupiter'
>   testImplementation 'org.testcontainers:rabbitmq'    // TestContainer 로 다른 컨테이너가 필요하면 얘만 교체하면 된다.
> }
> ```

### Serializable -> Jackson
> 역직렬화가 자바에 포함돼 있는 여러 보안 검사를 우회한다는 것은 잘 알려져 있다. 즉 보안에 취약하다.  
> Spring AMQP 의 경우 자바의 Serializable 인터페이스를 사용해서 직렬화를 처리할 수 있다. 메시지를 Serializable 처리하지 않고
> Jackson 을 통한 JSON 으로 처리하도록 Bean 을 등록한다.  
> ```java
> @Bean
> Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
>     return new Jackson2JsonMessageConverter();
> }
> ```

### log 확인
> Reactor, RabbitMQ, Spring Data 가 어떻게 협업하는지 알고 싶다면 application.yml 에 다음과 같이 로깅 레벨을 지정한다.  
> ```yaml
> logging:
>   level:
>     org.springframework.amqp: debug
>     org.springframework.messaging: debug
>     org.springframework.data: debug
>     reactor: debug
> ```
