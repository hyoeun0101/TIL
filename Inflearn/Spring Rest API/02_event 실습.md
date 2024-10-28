## 🍎 SpringBoot 프로젝트 세팅
- 프로젝트 생성 후 해야할 것
    - java 버전 확인하기
    - h2 의존성 runtime -> test 스코프로 수정
    - postgresql은 runtime -> complie 스코프로 수정.

- lombok 의존성의 `<optional>true</optional>` : 다른 프로젝트에서 이 프로젝트를 참조하였을 때 optional true를 설정한 의존성은 들어가지 않음.

- 의존성에서 test scope : 애플리케이션 실행 중에 사용되지 않는다. test를 실행할 때만 사용한다.

- 별 다른 데이터베이스 설정이 없으면 스프링 부트는 기본으로 h2를 실행함.
- @EnableAutoConfiguration
    - @SpringBootApplication에 있음.
    - 이 애노테이션으로 인해 스프링 부트가 빈을 자동으로 설정하여 실행한다. 이것 덕분에 어떤 설정 필요없이 스프링을 쉽게 실행할 수 있는 것이다.
    - 내장 웹 서버(톰캣)을 자동으로 설정한다.
    - JAR 패키징을 사용해서 손쉽게 사용할 수 있다.



### 🍎 Event 클래스 생성
- 필요한 설정
    - lombok plugin 설치하기.
    - settings > Annotation processor > enable annotation processing 키기

```java
@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "id")
public class Event{
    private Integer id;
}
```

1. 왜 @EqualsAndHashCode에서 of를 사용하는가?
    - 엔티티의 서로 상호 참조하는 것을 방지하기 위해. of를 사용하지 않으면 모든 필드에 대한 equals, hashCode를 생성한다. 엔티티가 서로 상호 참조를 한 경우 스택오버플로우가 발생할 수 있다.

2. 왜 @Builder를 사용할 때 @AllArgsConstructor가 필요한가?
    - 필요없다. @Builder는 모든 필드를 가진 생성자를 자동으로 만들어준다. 기본 생성자가 없다.
    - 따라서 기본 생성자를 만들기 위해 @NoArgsConstructor를 작성해야 한다. 기본 생성자가 있으면 @Builder는 생성자를 만들지 않아서 또 @AllArgsConstructor를 작성해줘야 한다.

3. @Data를 쓰지 않는 이유는?
    - @EqualsAndHashCode가 포함되어 있다. (첫 번째 질문과 동일한 이유) 이는 모든 필드에 대한 equals, hashCode이기 때문에 엔티티가 서로 상호 참조한 경우 스택 오버플로우가 발생한다.
    
4. 롬복 애노테이션들을 메타 애노테이션으로 만들어서 줄일 수 없나?
    - 없다. 롬복은 메타 애노테이션을 지원하지 않는다. 따라서 롬복의 애노테이션을 묶어서 따로 메타 애노테이션을 만들 수 없다.


## 🍎 EventController Test 만들기
```java
// @RunWith(SpringRunner.class) //JUnit5부터는 필요없음.
@WebMvcTest
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;

}
```
 
### @WebMvcTest
- 웹과 관련된 빈들이 모두 등록된다.이를 통해 MockMvc를 주입받아 사용할 수 있는 것!
- 웹과 관련 빈만 등록해준다. 따라서 `슬라이싱 테스트`라고 함. 
- 단위 테스트라 보긴 어려움. 딱 필요한 레이어만 테스트하는게 아니라 DispatcherServlet과 연관된 모든 빈들(Handler, Resolver 등)이 등록되어 있으니까.
- 단위 테스트보단 무겁고, 웹 테스트보단 가벼운 중간 정도
- 스프링 부트 슬라이스 테스트 = @WebMvcTest
- MockMvc 빈을 자동 설정 해준다. 따라서 그냥 가져와서 쓰면 됨.

요청과 응답을 검증할 때 사용.
### MockMvc
- 웹 서버를 띄우지 않고도 스프링 MVC (DispatcherServlet)가 요청을 처리하는 과정을 확인할 수 있기 때문에 컨트롤러 테스트용으로 자주 쓰임.
- MockMvc로 가짜 요청,응답을 만들어 검증할 수 있음. Test의 핵심 클래스. 웹 서버 띄우지 않고, DispatcherServlet를 구동한다. 
- 단위 테스트보단 무겁고, 웹 테스트보단 가벼운 중간 정도



## 🍎 EventRepository 만들고 테스트

### @MockBean
- 위의 @WebMvcTest는 웹 관련 빈만 등록하기 때문에 우리가 만든 빈은 등록하지 않는다. 따라서 @MockBean으로 빈을 직접 주입해야 한다.
```java
@WebMvcTest
public class EventControllerTests {

    @MockBean
    EventRepository eventRepository;
}
```


- 그런데 eventRepository는 Mock 객체이기 때문에 save같은 동작은 null을 반환한다. 이럴 때 사용하는게 Mokito이다. Mokito로 Mock 객체가 어떤 값을 리턴하는지 작성해줘야 한다.

```java
//given
Event event = Event.builder.build();

Mokito.when(eventRepository.save(event)).thenReturn(event);
// -> eventRepository.save(event)를 호출했을 때 event를 반환하라고 설정하는 것.
//when
//then
```

## 🍎 EventDto 만들기
- Dto에는 @Data써도 괜찮음.

### ModelMapper 사용하기
- DTO의 값을 엔티티로 옮길 때 사용. (EventDto -> Event)
- setter로 일일히 값을 넘겨주는게 아니라 modelMapper를 이용해서 엔티티를 사용할 수 있다. 단 Reflection을 사용하기 때문에 성능이 저하될 수도 있지만, 요즘 reflection api도 성능이 좋아져서 별 문제는 없다.

1. modelMapper 의존성 추가하기
```xml
<dependency>
    <groupId>org.modelmapper</groupId>
    <artifactId>modelmapper</artifactId>
    <version>3.2.0</version>
</dependency>
```
2. ModelMapper를 빈 등록하기
```java
@Bean
public ModelMapper modelMapper() {
    return new ModelMapper();
}
```

3. controller에서 주입받아 사용하기
```java
public ResponseEntity createEvent(@RequestBody EventDto eventDto) {

    Event event = modelMapper.map(eventDto, Event.class);
}
```


### mokito로 모킹한게 전달이 안된 이유.
- 컨트롤러에서 save하는 건 컨트롤러 내부에서 새로 생성한 Event 객체이기 때문. 모킹한 것과 실제 동작이 일치하지 않아서 모킹한 게 전달이 안된다.
```java

//Mokito로 모킹한 것.
Event event = Event.builder.build();
Mokito.when(eventRepository.save(event)).thenReturn(event);

// controller에서 실제 repository에 save하는 코드
Event event = modelMapper.map(eventDto, Event.class);
Event newEvent = eventRepository.save(event);
```

- 해결법 : 테스트에서 모킹하지 않고 스프링 테스트 쓰기
    - @WebMvcTest를 사용하지 않고, @SpringBootTest, @AutoConfigureMockMvc를 사용한다. @MockBean은 삭제.

- 웹 관련 테스트는 SpringBootTest 이용한다.

### @SpringBootTest, @AutoConfigureMockMvc
- 모킹한 DispatcherServlet을 만든다. 기본값이 Mock임.
- 웹 관련 테스트는 슬라이스 테스트보다 SpringBootTest가 낫다. 안그럼 mocking할게 너무 많다.


## 🍎 입력값 이외에 에러
- 설정에 다음 설정을 넣으면 됨.
```yml
spring:
  jackson:
    deserialization:
      fail-on-unknown-properties: true
```
### Errors
reject()호출하면 Global 에러, rejectValue() 호출하면 Field 에러




### @JsonComponent
- SpringBoot가 제공. serialzier를 objectMapper에 등록.
- ObjectMapper가 Errors 객체를 직렬화할 때 해당 Serializer를 사용한다. 
```java
class ErrorsSerializer implements JsonSerializer<Errors> { ... }
```


### String 빈 값 체크하는 법
- java 11 이후에 isBlank() 등장. (그 전엔 trim 한 후, isEmpty했어야 함)

```java
value != null && !value.isBlank()
```


### 도메인 테스트 코드 리팩토링
- 파라미터로 데이터 입력받기
```java
@ParameterizedTest
@MethodSource("paramsForTestFree")
public void testFree(int basePrice, int maxPrice, boolean isFree) {
    //given
    Event event = Event.builder()
            .basePrice(basePrice)
            .maxPrice(maxPrice)
            .build();
    //when
    event.update();
    //then
    assertThat(event.isFree()).isEqualTo(isFree);
}

private static Object[] paramsForTestFree() {
    return new Object[] {
            new Object[] {0,0,true},
            new Object[] {0,100, false},
            new Object[] {100,0,false},
            new Object[] {100,200,false}
    };
}
```