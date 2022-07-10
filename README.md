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
