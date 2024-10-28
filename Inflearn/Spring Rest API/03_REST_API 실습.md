### HATEOAS와 Self-Descriptive Message 적용
Rest 리소스를 제공.
라이브러리.

### HATEOAS?
- hypermedia as the engine of application state. 어플리케이션 상태의 엔진으로서의 하이퍼미디어. 어플리케이션 상태를 바꾸는 하이퍼미디어.
- HATEOAS하다 = 하이퍼미디어로 애플리케이션의 상태를 바꿀 수 있다. = 화면을 바꿀 수 있다.
- 하이퍼미디어로 애플리케이션의 상태 전이가 가능하다.
- Rest application architecture의 컴포넌트 중 하나고, 
하이퍼미디어를 사용해서 동적으로 정보를 주고받음.
```
<link rel = "" href="">
```
- 서버와 클라이언트는 rel(관계)만 보고 처리를 한다. link는 바껴도 상관없음.
- 애플리케이션 상태에 따라 link 정보가 바뀜.

## 🍎 Spring HATEOAS 기능 핵심
- 링크 만드는 기능 제공
    - 문자열 가지고 만들기
    - 컨트롤러와 메소드로 만들기 -> linkTo().slash(), methodOn()
- 리소스 만드는 기능
    - 리소스: 데이터 + 링크
- 링크 찾아주는 기능
    - Tranverson
    - LinkDiscoverers
    - 강좌에서 다루진 X

### 링크에 들어가는 정보
- href : hypermedia reference. url 설정
- rel : relation. 현재 리소스와의 관계를 표현
     - self : 자기 자신에 대한 링크
     - profile : 응답 본문에 대한 설명 문서 링크
     - update : 수정
     - query-events : 조회
     - 이외 deposite, transfer, withdraw 등

- event를 생성했을 때 어떠한 링크 정보를 보여줄 수 있나?
    - self, profile
    - update-event : 이벤트 수정할 수 있는 링크
    - query-events : 이벤트를 조회할 수 있는 링크
    

## 🍎 응답에 링크 정보 넣기 
- Event를 EventResource로 변환한다.
- ResourceSupport 상속받는 클래스 생성.
- @JsonUnwrapped : 직렬화할 때 매핑을 하지 않는다. `{"event" : { "id" : "123", "name":"test" }}` 이렇게 하지 않고, `{ "id" : "123", "name":"test" }` 

```java
public class EventResource extends ResourceSupport {
    @JsonUnwrapped
    private Event event;

    public EventResource(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }
}
```
- EntityModel<T> : Spring Hateoas에서 지원. EntityModel로 Wrapping하면 Spring HATEOAS가 자동으로 표준 링크(_self 등)을 포함시킬 수 있다.

```java
public class EventResource extends EntityModel<Event> {
    public EventResource(Event event) {
        super(event);
    }
}
```

- ObjectMapper가 직렬화할 때 BeanSerializer를 사용한다.
- 스프링에서 HATEOAS를 사용하려면 @EnableHypermediaSupport를 써서 빈 설정해줘야 하는데, 스프링 부트가 자동으로 설정해줘서 아무 설정없이 바로 HATEOAS를 사용할 수 있었다.

## 🍎 Spring Rest Docs
- Spring MVC Test 를 사용해서 문서의 일부분을 생성해내는 라이브러리.

- Sweager보다 Rest Docs를 선호하는 이유 : api를 변경했을 때, 테스트 코드도 변경해야 하는데 그럼 문서도 같이 바뀐다. 코드는 변경되었는데, 문서에 반영안되는 경우를 방지할 수 있음.

1. pom.xml에 plugin 설정 추가하기
```xml
<!-- 해당 설정이 /target/generate-docs에 index.html 생성함 -->
<plugin>
    <groupId>org.asciidoctor</groupId>
    <artifactId>asciidoctor-maven-plugin</artifactId> 
    <version>2.2.1</version>
    <executions>
        <execution>
            <id>generate-docs</id>
            <phase>prepare-package</phase> <!--해당 기능을 prepare-package에 넣어줌.-->
            <goals>
                <goal>process-asciidoc</goal> <!-- src/main/asciidoc에 있는 모든 아스키닥 문서를 html로 만들어줌 -->
            </goals>
            <configuration>
                <backend>html</backend>
                <doctype>book</doctype>
            </configuration>
        </execution>
    </executions>
    <dependencies>
        <dependency>
            <groupId>org.springframework.restdocs</groupId>
            <artifactId>spring-restdocs-asciidoctor</artifactId>
            <version>${spring-restdocs.version}</version>
        </dependency>
    </dependencies>
</plugin>

<!-- 해당 설정이 /static/docs에 index.html 생성함 -->
<plugin>
    <artifactId>maven-resources-plugin</artifactId>
    <version>2.7</version>
    <executions>
        <execution>
            <id>copy-resources</id> <!--generated-docs에서 카피. 때문에 이 플러그인 설정이 뒤에 와야함-->
            <phase>prepare-package</phase> <!--해당 기능을 prepare-package에 넣어줌.-->
            <goals>
                <goal>copy-resources</goal>
            </goals>
            <configuration>
                <outputDirectory> <!--빌드된 디렉토리 기준. 즉 target 디렉토리에 생성됨-->
                    ${project.build.outputDirectory}/static/docs
                </outputDirectory>
                <resources>
                    <resource>
                        <directory>
                            ${project.build.directory}/generated-docs
                        </directory>
                    </resource>
                </resources>
            </configuration>
        </execution>
    </executions>
</plugin>
```
2. test 클래스에 @AutoConfigureRestDcos 추가. test코드에서 document() 작성 - 해당 테스트 메서드를 실행하면 target/generated-snippets에 조각 파일들이 생성됨.
3. test환경에 rest docs 포맷 설정 추가.
```java

@TestConfiguration
public class RestDocsConfiguration {
    @Bean
    public RestDocsMockMvcConfigurationCustomizer restDocsMockMvcConfigurationCustomizer() {

        return configurer -> {
            configurer.operationPreprocessors()
                    .withRequestDefaults(prettyPrint())
                    .withResponseDefaults(prettyPrint());
        };

    }
}
```
```java
@Import(RestDocsConfiguration.class) //설정 빈 import!!
public class EventControllerTest() {...}
```

### API 문서 만들기 - 각종 문서 파일들
```java
.andDo(document("create-event",
    links( //링크 문서화
        linkWithRel("self").description("link to self"),
        linkWithRel("query-events").description("link to query events"),
        linkWithRel("update-event").description("link to update event"),
        linkWithRel("profile").description("link to profile"),
    ),
    requestHeaders( // 요청 헤더 문서화
            headerWithName(HttpHeaders.ACCEPT).description("accept header"),
            headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
    ),
    requestFields(...), //요청 필드 문서화
    responseHeaders(...), //응답 헤더 문서화
    responseFields(...), //응답 필드 문서화


))
```
-> target/generated-snippets에 .adoc 문서 생성.
- links() => links.adcos, requestHeaders() => request-headers.adoc
- relaxedXXXXX() 메서드. ex) relaxedResponseFileds() : 응답의 일부분만 문서화할 수 있음.
    - 장점 : 문서 일부분만 테스트할 수 있다.
    - 단점 : 정확한 문서를 생성하지 못한다.

### Spring Docs 문서 빌드(각종 문서 파일들로 문서 만들기)
1. pom.xml에 plugin 설정
2. src/main/asciidoc/index.adoc 생성
3. mvn package 실행
    - 테스트 실행하면 docs 조각들이 생성됨.
    - 플러그인 때문에 최종적으로 문서 생성되고, 생성된 문서는 스프링 부트가 기본적으로 지원하는 static 디렉토리에 들어감.
    - `target/classes/static/docs/index.html` 생성 -> `localhost:8080/docs/index.html`로 접근 가능. 

## 🍎 테스트용 DB와 설정 분리하기
1. pom.xml에서 h2의 scope는 test로, postgresql의 scope는 runtime으로!
```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```
2. application.yml 설정 추가

```yml
spring:
  datasource:
    username: postgres
    password: pass
    url: jdbc:postgresql://localhost:5432/postgres
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: create-drop
    properties:
      hibernate:
        format_sql: true
logging:
  level:
    org.hibernate.SQL: debug
    org.hibernate.type: trace


```

- `@SpringBootTest`는 `@SpringBootApplication`이 붙어있는 곳 가서 빈들 등록하고, 마찬가지로 설정 파일도 사용한다. 따라서 테스트의 설정 파일 다르게 하려면 test에 설정파일을 추가해야 한다.
    - test> resource> application.yml 추가
    - project structure > modules에서 해당 파일을 test resource로 지정
- test 설정이 main을 덮어씌움.

2. 테스트 설정 파일 추가. application-test.yml

```yml
spring:
  datasource:
    username: sa
    password:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    hikari:
      jdbc-url: jdbc:h2:mem:testdb
  jpa:
    properties:
      hibernate:
        dialect: org.hibernate.dialect.H2Dialect
```

- test클래스에 @ActiveProfiles("test") 추가


```
# 소스 코드 컴파일
$ javac Hello.java

# 패키징
$ jar cvfe Hello.jar Hello Hello.class

# 실행
$ java -jar Hello.jar
Hello, World!
```


api의 진입점이 필요함.